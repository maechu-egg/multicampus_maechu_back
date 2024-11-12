package com.multipjt.multi_pjt.badge.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multipjt.multi_pjt.badge.dao.MemberBadgeMapper;
import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeResponseDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordRequestDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordResponseDTO;
import org.springframework.http.HttpStatus;

@Service
public class BadgeService implements IBadgeService {

    private static final Logger logger = LoggerFactory.getLogger(BadgeService.class);

    private final MemberBadgeMapper memberBadgeMapper;
    private final UserActivityRecordMapper userActivityRecordMapper;
    private final ActivityPointService activityPointService;

    @Autowired
    public BadgeService(MemberBadgeMapper memberBadgeMapper, UserActivityRecordMapper userActivityRecordMapper, 
                        ActivityPointService activityPointService) {
        this.memberBadgeMapper = memberBadgeMapper;
        this.userActivityRecordMapper = userActivityRecordMapper;
        this.activityPointService = activityPointService;
    }

    // 사용자 점수 조회
    @Override
    public BigDecimal getCurrentPoints(int memberId) {
        BigDecimal currentPoints = memberBadgeMapper.getCurrentPoints(memberId);
        return currentPoints != null ? currentPoints : BigDecimal.ZERO;
    }

    // 뱃지 등급 조회
    @Override
    public String getBadgeLevel(BigDecimal currentPoints) {
        if (currentPoints.compareTo(BigDecimal.valueOf(100)) >= 0) return "다이아";
        if (currentPoints.compareTo(BigDecimal.valueOf(70)) >= 0) return "플래티넘";
        if (currentPoints.compareTo(BigDecimal.valueOf(50)) >= 0) return "골드";
        if (currentPoints.compareTo(BigDecimal.valueOf(30)) >= 0) return "실버";
        if (currentPoints.compareTo(BigDecimal.valueOf(10)) >= 0) return "브론즈";
        return "기본";
    }

    // 다음 뱃지까지 필요한 점수 조회
    @Override
    public BigDecimal getPointsToNextBadge(BigDecimal currentPoints) {
        if (currentPoints.compareTo(new BigDecimal("10")) < 0) return new BigDecimal("10").subtract(currentPoints);
        if (currentPoints.compareTo(new BigDecimal("30")) < 0) return new BigDecimal("30").subtract(currentPoints);
        if (currentPoints.compareTo(new BigDecimal("50")) < 0) return new BigDecimal("50").subtract(currentPoints);
        if (currentPoints.compareTo(new BigDecimal("70")) < 0) return new BigDecimal("70").subtract(currentPoints);
        if (currentPoints.compareTo(new BigDecimal("100")) < 0) return new BigDecimal("100").subtract(currentPoints);
        return BigDecimal.ZERO; // 이미 최고 등급인 경우
    }

    // 사용자 점수 업데이트
    @Override
    public void updateUserPoints(int memberId, BigDecimal points) {
        UserActivityRecordRequestDTO activityRecord = new UserActivityRecordRequestDTO();
        activityRecord.setMemberId(memberId);
        activityRecord.setCreatedDate(LocalDateTime.now());
        userActivityRecordMapper.insertActivity(activityRecord);

        MemberBadgeRequestDTO badgeRequest = new MemberBadgeRequestDTO();
        badgeRequest.setMember_id(memberId);
        badgeRequest.setCurrent_points(points.floatValue());
        memberBadgeMapper.updateBadge(badgeRequest);
    }

    // 사용자 점수 순위 조회
    @Override
    public List<Map<String, Object>> getUserRankings() {
        List<MemberBadgeResponseDTO> badges = memberBadgeMapper.getAllBadges();
        return badges.stream()
                .map(badge -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("memberId", badge.getMember_id());
                    map.put("currentPoints", badge.getCurrent_points());
                    map.put("badgeLevel", getBadgeLevel(BigDecimal.valueOf(badge.getCurrent_points())));
                    map.put("nickname", badge.getNickname());
                    return map;
                })
                .sorted((a, b) -> Float.compare((Float) b.get("currentPoints"), (Float) a.get("currentPoints")))
                .collect(Collectors.toList());
    }

    // 사용자 활동을 처리하는 메서드
    @Transactional
    public void processUserActivities(int memberId) {
        // 1. 회원의 운동 기록 조회
        List<UserActivityRecordResponseDTO> exerciseActivities = userActivityRecordMapper.getActivitiesFromExercises(memberId);
        
        // 2. 오늘 날짜와 비교하여 점수 부여 로직
        LocalDate today = LocalDate.now();
        boolean hasExerciseToday = exerciseActivities.stream()
            .anyMatch(activity -> activity.getCreatedDate().toLocalDate().isEqual(today));

        if (hasExerciseToday) {
            // 점수 부여 로직
            UserActivityRecordRequestDTO activityRecord = new UserActivityRecordRequestDTO();
            activityRecord.setActivityType("exercise");
            activityRecord.setMemberId(memberId);
            activityRecord.setCreatedDate(LocalDateTime.now());
            userActivityRecordMapper.insertActivity(activityRecord);
        }
        // 3. 나머지 활동 처리 (포스트, 댓글, 식단 등)
        List<UserActivityRecordResponseDTO> postActivities = userActivityRecordMapper.getActivitiesFromPosts(memberId);
        List<UserActivityRecordResponseDTO> commentActivities = userActivityRecordMapper.getActivitiesFromComments(memberId);
        List<UserActivityRecordResponseDTO> dietActivities = userActivityRecordMapper.getActivitiesFromDiets(memberId);

        for (UserActivityRecordResponseDTO activity : postActivities) {
            insertActivityRecord(memberId, activity);
        }
        for (UserActivityRecordResponseDTO activity : commentActivities) {
            insertActivityRecord(memberId, activity);
        }
        for (UserActivityRecordResponseDTO activity : dietActivities) {
            insertActivityRecord(memberId, activity);
        }

        // 4. 점수 업데이트
        updateMemberBadgePoints(memberId);
    }

    private void insertActivityRecord(int memberId, UserActivityRecordResponseDTO activity) {
        UserActivityRecordRequestDTO activityRecord = new UserActivityRecordRequestDTO();
        activityRecord.setActivityType(activity.getActivityType());
        activityRecord.setMemberId(memberId);
        activityRecord.setCreatedDate(activity.getCreatedDate());
        userActivityRecordMapper.insertActivity(activityRecord);
    }

    private void updateMemberBadgePoints(int memberId) {
        // 사용자의 총 점수 계산
        List<BigDecimal> pointsList = userActivityRecordMapper.getTotalPointsByMemberId(memberId);
        BigDecimal totalPoints = pointsList.isEmpty() ? BigDecimal.ZERO : pointsList.get(0);
        
        // 현재 뱃지 정보 조회
        MemberBadgeResponseDTO currentBadge = memberBadgeMapper.getBadgeByMemberId(memberId);
        // 뱃지 레벨 결정
        String newBadgeLevel = getBadgeLevel(totalPoints);
        
        // 뱃지 정보 업데이트
        if (currentBadge != null) {
            // 현재 점수와 뱃지 레벨 업데이트
            MemberBadgeRequestDTO badgeRequest = new MemberBadgeRequestDTO();
            badgeRequest.setMember_id(memberId);
            badgeRequest.setCurrent_points(totalPoints.floatValue());
            badgeRequest.setBadge_level(newBadgeLevel);
            
            // 뱃지 업데이트
            memberBadgeMapper.updateBadge(badgeRequest);
        } else {
            // 뱃지가 없는 경우 새로 생성
            createBadge(memberId);
        }
    }

    @Override
    public void createBadge(int memberId) {
        // 기본 점수와 뱃지 레벨 설정
        float defaultPoints = 0.0f; // 기본 점수
        String defaultBadgeLevel = "기본"; // 기본 뱃지 레벨

        // 뱃지 생성 요청 DTO
        MemberBadgeRequestDTO badgeRequest = new MemberBadgeRequestDTO();
        badgeRequest.setMember_id(memberId);
        badgeRequest.setCurrent_points(defaultPoints);
        badgeRequest.setBadge_level(defaultBadgeLevel);

        // 뱃지 삽입
        memberBadgeMapper.insertBadge(badgeRequest);
    }

   
}
