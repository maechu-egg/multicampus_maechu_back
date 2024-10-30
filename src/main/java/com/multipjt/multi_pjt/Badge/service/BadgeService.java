package com.multipjt.multi_pjt.badge.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multipjt.multi_pjt.badge.dao.MemberBadgeMapper;
import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeResponseDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordRequestDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordResponseDTO;

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
    public BigDecimal getCurrentPoints(Long memberId) {
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
    public void updateUserPoints(Long memberId, BigDecimal points) {
        UserActivityRecordRequestDTO activityRecord = new UserActivityRecordRequestDTO();
        activityRecord.setMember_id(memberId.intValue());
        activityRecord.setPoints(points.floatValue());
        activityRecord.setCreated_date(LocalDateTime.now());
        userActivityRecordMapper.insertActivity(activityRecord);

        MemberBadgeRequestDTO badgeRequest = new MemberBadgeRequestDTO();
        badgeRequest.setMember_id(memberId.intValue());
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
                    return map;
                })
                .sorted((a, b) -> Float.compare((Float) b.get("currentPoints"), (Float) a.get("currentPoints")))
                .collect(Collectors.toList());
    }

    // 사용자 활동을 처리하는 메서드
    @Transactional
    public void processUserActivities(Long memberId) {
        // posts와 comments에서 활동 가져오기
        List<UserActivityRecordResponseDTO> postActivities = userActivityRecordMapper.getActivitiesFromPosts(memberId);
        List<UserActivityRecordResponseDTO> commentActivities = userActivityRecordMapper.getActivitiesFromComments(memberId);

        // 모든 활동을 useractivityrecord 테이블에 삽입
        for (UserActivityRecordResponseDTO activity : postActivities) {
            insertActivityRecord(memberId, activity);
        }
        for (UserActivityRecordResponseDTO activity : commentActivities) {
            insertActivityRecord(memberId, activity);
        }

        // 점수 업데이트
        updateMemberBadgePoints(memberId);
    }

    private void insertActivityRecord(Long memberId, UserActivityRecordResponseDTO activity) {
        UserActivityRecordRequestDTO activityRecord = new UserActivityRecordRequestDTO();
        activityRecord.setActivity_type(activity.getActivityType());
        activityRecord.setPoints(activity.getPoints());
        activityRecord.setMember_id(memberId.intValue());
        activityRecord.setCreated_date(activity.getCreatedDate());
        userActivityRecordMapper.insertActivity(activityRecord);
    }

    private void updateMemberBadgePoints(Long memberId) {
        // 사용자의 총 점수 계산
        List<BigDecimal> pointsList = userActivityRecordMapper.getTotalPointsByMemberId(memberId);
        BigDecimal totalPoints = pointsList.isEmpty() ? BigDecimal.ZERO : pointsList.get(0);
        
        // 현재 뱃지 정보 조회
        MemberBadgeResponseDTO currentBadge = memberBadgeMapper.getBadgeByMemberId(memberId.intValue());
        // 뱃지 레벨 결정
        String newBadgeLevel = getBadgeLevel(totalPoints);
        
        // 뱃지 정보 업데이트
        if (currentBadge != null) {
            // 현재 점수와 뱃지 레벨 업데이트
            MemberBadgeRequestDTO badgeRequest = new MemberBadgeRequestDTO();
            badgeRequest.setMember_id(memberId.intValue());
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
    public void createBadge(Long memberId) {
        // 기본 점수와 뱃지 레벨 설정
        float defaultPoints = 0.0f; // 기본 점수
        String defaultBadgeLevel = "기본"; // 기본 뱃지 레벨

        // 뱃지 생성 요청 DTO
        MemberBadgeRequestDTO badgeRequest = new MemberBadgeRequestDTO();
        badgeRequest.setMember_id(memberId.intValue());
        badgeRequest.setCurrent_points(defaultPoints);
        badgeRequest.setBadge_level(defaultBadgeLevel);

        // 뱃지 삽입
        memberBadgeMapper.insertBadge(badgeRequest);
    }
}
