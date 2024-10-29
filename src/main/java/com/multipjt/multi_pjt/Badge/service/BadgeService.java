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

    @Override
    public void processUserActivities(Long memberId) {
        // 사용자의 활동을 조회
        List<UserActivityRecordResponseDTO> activities = userActivityRecordMapper.getActivitiesByMemberId(memberId);
        
        float totalPoints = 0;

        // 각 활동에 대해 점수를 계산하고 업데이트
        for (UserActivityRecordResponseDTO activity : activities) {
            float points = activityPointService.calculateActivityPoints(activity.getActivityType());
            totalPoints += points;

            // 활동 기록을 데이터베이스에 삽입
            UserActivityRecordRequestDTO activityRecord = new UserActivityRecordRequestDTO();
            activityRecord.setActivity_type(activity.getActivityType());
            activityRecord.setPoints(points);
            activityRecord.setMember_id(memberId.intValue());
            activityRecord.setCreated_date(LocalDateTime.now());
            userActivityRecordMapper.insertActivity(activityRecord);
        }

        // 총 점수 업데이트
        updateMemberBadgePoints(memberId, totalPoints);
    }

    private void updateMemberBadgePoints(Long memberId, float totalPoints) {
        // 현재 점수 조회
        BigDecimal currentPoints = getCurrentPoints(memberId);
        float newTotalPoints = currentPoints.floatValue() + totalPoints;

        // MemberBadge의 current_points 업데이트
        MemberBadgeRequestDTO badgeRequest = new MemberBadgeRequestDTO();
        badgeRequest.setMember_id(memberId.intValue());
        badgeRequest.setCurrent_points(newTotalPoints); // 현재 점수 업데이트
        badgeRequest.setBadge_level(getBadgeLevel(BigDecimal.valueOf(newTotalPoints))); // 뱃지 레벨 업데이트
        memberBadgeMapper.updateBadge(badgeRequest);
    }
}
