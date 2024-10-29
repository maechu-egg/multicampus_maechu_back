package com.multipjt.multi_pjt.badge.service;

import java.math.BigDecimal;
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
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordRequsetDTO;

@Service
public class BadgeService {

    private static final Logger logger = LoggerFactory.getLogger(BadgeService.class);

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
        return "기본";
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

    @Transactional
    public void processMemberActivity(Long memberId) {
        try {
            logger.info("Processing member activities for member: {}", memberId);
            
            // posts, comments, diet, exercise 테이블에서 활동 내역 가져오기
            List<Map<String, Object>> activityRecords = memberBadgeMapper.getMemberActivity(memberId);
            
            if (activityRecords.isEmpty()) {
                logger.info("No activity records found for member: {}", memberId);
                return;
            }

            // 활동 기록을 UserActivityRecord 테이블에 반영
            float totalPoints = 0; // 총 점수 초기화
            for (Map<String, Object> record : activityRecords) {
                try {
                    // UserActivityRecord에 활동 기록 삽입
                    UserActivityRecordRequsetDTO activityRecord = createActivityRecord(record);
                    userActivityRecordMapper.insertActivity(activityRecord);
                    totalPoints += (float) record.get("points"); // 총 점수 누적
                    logger.debug("Activity record inserted: {}", record);
                } catch (Exception e) {
                    logger.error("Error inserting activity record: {}", record, e);
                    // 개별 레코드 실패는 기록하고 계속 진행
                }
            }

            // MemberBadge의 current_points 업데이트
            updateMemberBadgePoints(memberId, totalPoints);
            logger.info("Total points updated for member {}: {}", memberId, totalPoints);

        } catch (Exception e) {
            logger.error("Error processing member activities", e);
            throw new RuntimeException("멤버 활동 처리 중 오류가 발생했습니다", e);
        }
    }

    public int getCurrentPoints(int memberId) {
        try {
            MemberBadgeResponseDTO currentBadge = memberBadgeMapper.getBadgeByMemberId(memberId);
            if (currentBadge == null) {
                throw new IllegalArgumentException("멤버 ID에 해당하는 뱃지를 찾을 수 없습니다: " + memberId);
            }
            return (int) currentBadge.getCurrent_points();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error getting current points for member: {}", memberId, e);
            throw new RuntimeException("포인트 조회 중 오류가 발생했습니다", e);
        }
    }

    public void updateMemberBadgePoints(Long memberId, float totalPoints) {
        MemberBadgeRequestDTO badgeRequest = new MemberBadgeRequestDTO();
        badgeRequest.setMember_id(memberId.intValue());
        badgeRequest.setCurrent_points(totalPoints); // 총 점수 업데이트
        memberBadgeMapper.updateBadge(badgeRequest);
    }

    private UserActivityRecordRequsetDTO createActivityRecord(Map<String, Object> record) {
        UserActivityRecordRequsetDTO activityRecord = new UserActivityRecordRequsetDTO();
        activityRecord.setMember_id((Integer) record.get("member_id")); // 적절한 필드 매핑
        activityRecord.setPoints((Float) record.get("points")); // 적절한 필드 매핑
        return activityRecord;
    }

}
