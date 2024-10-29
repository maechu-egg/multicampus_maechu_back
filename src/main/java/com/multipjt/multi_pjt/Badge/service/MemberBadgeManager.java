package com.multipjt.multi_pjt.badge.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multipjt.multi_pjt.badge.dao.MemberBadgeMapper;
import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeResponseDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordRequsetDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordResponseDTO;

@Service
public class MemberBadgeManager {
    private static final Logger logger = LoggerFactory.getLogger(MemberBadgeManager.class);

    private final ActivityPointService activityPointService;
    private final BadgeService badgeService;
    private final MemberBadgeMapper memberBadgeMapper;
    private final UserActivityRecordMapper userActivityRecordMapper;

    public MemberBadgeManager(ActivityPointService activityPointService, 
                            BadgeService badgeService, 
                            MemberBadgeMapper memberBadgeMapper, 
                            UserActivityRecordMapper userActivityRecordMapper) {
        this.activityPointService = activityPointService;
        this.badgeService = badgeService;
        this.memberBadgeMapper = memberBadgeMapper;
        this.userActivityRecordMapper = userActivityRecordMapper;
    }

    // 활동을 처리하고 포인트와 뱃지를 업데이트하는 메서드
    @Transactional
    public int processActivity(int memberId, String activityType) {
        try {
            logger.info("Processing activity for member: {}, activity type: {}", memberId, activityType);
            
            // 활동에 따른 포인트 계산
            float points = activityPointService.calculateActivityPoints(activityType);
        
            // 포인트가 0이면 잘못된 활동 유형
            if (points == 0) {
                logger.warn("Invalid activity type: {}", activityType);
                throw new IllegalArgumentException("유효하지 않은 활동 유형입니다: " + activityType);
            }

            // 일일 제한이 있는 활동(포스트, 댓글)인 경우 체크
            if (isDailyActivity(activityType)) {
                if (userActivityRecordMapper.countTodayActivity(memberId, activityType) > 0) {
                    logger.info("Daily activity limit reached for member: {}, activity: {}", 
                              memberId, activityType);
                    return getCurrentPoints(memberId);
                }
                logger.info("Processing daily activity: {}", activityType);
            }

            // 활동 기록 생성
            UserActivityRecordRequsetDTO activityRecord = createActivityRecord(memberId, activityType, points);
            userActivityRecordMapper.insertActivity(activityRecord);

            // 뱃지 처리
            return processAndUpdateBadge(memberBadgeMapper.getBadgeByMemberId(memberId), memberId, points);

        } catch (Exception e) {
            logger.error("Error processing activity", e);
            throw new RuntimeException("활동 처리 중 오류가 발생했습니다", e);
        }
    }

    private boolean isDailyActivity(String activityType) {
        return activityType.equals("post") || 
               activityType.equals("comment"); // diet와 exercise 제거하고 comment만 추가
    }

    private UserActivityRecordRequsetDTO createActivityRecord(int memberId, 
                                                            String activityType, 
                                                            float points) {
        UserActivityRecordRequsetDTO record = new UserActivityRecordRequsetDTO();
        record.setMember_id(memberId);
        record.setActivity_type(activityType);
        record.setPoints(points);
        return record;
    }

    private int processAndUpdateBadge(MemberBadgeResponseDTO currentBadge, 
                                    int memberId, 
                                    float points) {
        if (currentBadge == null) {
            return createNewBadge(memberId, points);
        } else {
            return updateExistingBadge(currentBadge, points);
        }
    }

    private int createNewBadge(int memberId, float points) {
        MemberBadgeRequestDTO newBadge = new MemberBadgeRequestDTO();
        newBadge.setMember_id(memberId);
        newBadge.setCurrent_points(points);
        newBadge.setBadge_level(badgeService.getBadgeLevel(BigDecimal.valueOf(points)));
        memberBadgeMapper.insertBadge(newBadge);
        return (int) points;
    }

    private int updateExistingBadge(MemberBadgeResponseDTO currentBadge, float points) {
        float updatedPoints = points + currentBadge.getCurrent_points();
        String newBadgeLevel = badgeService.getBadgeLevel(BigDecimal.valueOf(updatedPoints));

        MemberBadgeRequestDTO updatedBadge = new MemberBadgeRequestDTO();
        updatedBadge.setBadge_id(currentBadge.getBadge_id());
        updatedBadge.setCurrent_points(updatedPoints);
        updatedBadge.setBadge_level(newBadgeLevel);
        updatedBadge.setMember_id(currentBadge.getMember_id());

        memberBadgeMapper.updateBadge(updatedBadge);
        return (int) updatedPoints;
    }

    // 멤버의 활동 기록 처리
    @Transactional
    public void processMemberActivity(Long memberId) {
        try {
            logger.info("Processing member activities for member: {}", memberId);
            
            // 멤버의 활동 내역 가져오기
            List<UserActivityRecordResponseDTO> activityRecords = memberBadgeMapper.getMemberActivity(memberId);
            
            if (activityRecords.isEmpty()) {
                logger.info("No activity records found for member: {}", memberId);
                return;
            }

            // 활동 기록을 UserActivityRecord 테이블에 반영
            for (UserActivityRecordResponseDTO record : activityRecords) {
                try {
                    UserActivityRecordRequsetDTO requestDTO = new UserActivityRecordRequsetDTO();
                    requestDTO.setMember_id(record.getMember_id());
                    requestDTO.setActivity_type(record.getActivity_type());
                    requestDTO.setPoints(record.getPoints());
                    userActivityRecordMapper.insertActivity(requestDTO);
                    logger.debug("Activity record inserted: {}", record);
                } catch (Exception e) {
                    logger.error("Error inserting activity record: {}", record, e);
                    // 개별 레코드 실패는 기록하고 계속 진행
                }
            }
        } catch (Exception e) {
            logger.error("Error processing member activities", e);
            throw new RuntimeException("멤버 활동 처리 중 오류가 발생했습니다", e);
        }
    }

    // 현재 포인트를 반환하는 메서드
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
}
