package com.multipjt.multi_pjt.badge.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.badge.dao.MemberBadgeMapper;
import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeResponseDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordRequsetDTO;

@Service
public class BadgeService {

    private final MemberBadgeMapper memberBadgeMapper;
    private final UserActivityRecordMapper userActivityRecordMapper;

    @Autowired
    public BadgeService(MemberBadgeMapper memberBadgeMapper, UserActivityRecordMapper userActivityRecordMapper) {
        this.memberBadgeMapper = memberBadgeMapper;
        this.userActivityRecordMapper = userActivityRecordMapper;
    }

    // 사용자 점수 조회
    public BigDecimal getCurrentPoints(Long memberId) {
        // 데이터베이스에서 BigDecimal로 가져오기
        BigDecimal currentPoints = memberBadgeMapper.getCurrentPoints(memberId);
        return currentPoints != null ? currentPoints : BigDecimal.ZERO;
    }

    // 뱃지 등급 조회
    public String getBadgeLevel(BigDecimal currentPoints) {
        if (currentPoints.compareTo(new BigDecimal("100")) >= 0) return "다이아";
        if (currentPoints.compareTo(new BigDecimal("70")) >= 0) return "플래티넘";
        if (currentPoints.compareTo(new BigDecimal("50")) >= 0) return "골드";
        if (currentPoints.compareTo(new BigDecimal("30")) >= 0) return "실버";
        if (currentPoints.compareTo(new BigDecimal("10")) >= 0) return "브론즈";
        return "없음";
    }

    // 다음 뱃지까지 필요한 점수 조회
    public BigDecimal getPointsToNextBadge(BigDecimal currentPoints) {
        if (currentPoints.compareTo(new BigDecimal("10")) < 0) return new BigDecimal("10").subtract(currentPoints);
        if (currentPoints.compareTo(new BigDecimal("30")) < 0) return new BigDecimal("30").subtract(currentPoints);
        if (currentPoints.compareTo(new BigDecimal("50")) < 0) return new BigDecimal("50").subtract(currentPoints);
        if (currentPoints.compareTo(new BigDecimal("70")) < 0) return new BigDecimal("70").subtract(currentPoints);
        if (currentPoints.compareTo(new BigDecimal("100")) < 0) return new BigDecimal("100").subtract(currentPoints);
        return BigDecimal.ZERO; // 이미 최고 등급인 경우
    }

    // 사용자 점수 업데이트
    public void updateUserPoints(Long memberId, BigDecimal points) {
        // 활동 기록을 UserActivityRecord 테이블에 삽입
        UserActivityRecordRequsetDTO activityRecord = new UserActivityRecordRequsetDTO();
        activityRecord.setMember_id(memberId.intValue());
        activityRecord.setPoints(points.floatValue());
        userActivityRecordMapper.insertActivity(activityRecord);

        // 뱃지 업데이트
        MemberBadgeRequestDTO badgeRequest = new MemberBadgeRequestDTO();
        badgeRequest.setMember_id(memberId.intValue());
        badgeRequest.setCurrent_points(points.floatValue()); // 현재 점수 업데이트
        memberBadgeMapper.updateBadge(badgeRequest);
    }

    // 사용자 점수 순위 조회
    public List<Map<String, Object>> getUserRankings() {
        // 모든 회원의 뱃지 조회
        List<MemberBadgeResponseDTO> badges = memberBadgeMapper.getAllBadges();
        return badges.stream()
                .map(badge -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("memberId", badge.getMember_id());
                    map.put("currentPoints", badge.getCurrent_points());
                    map.put("badgeLevel", getBadgeLevel(BigDecimal.valueOf(badge.getCurrent_points())));
                    return map;
                })
                .sorted((a, b) -> Float.compare((Float) b.get("currentPoints"), (Float) a.get("currentPoints"))) // 포인트 기준 내림차순 정렬
                .collect(Collectors.toList());
    }


}
