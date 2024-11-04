package com.multipjt.multi_pjt.badge.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multipjt.multi_pjt.badge.dao.MemberBadgeMapper;
import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordRequestDTO;

@Service
public class MemberBadgeManager {
    private static final Logger logger = LoggerFactory.getLogger(MemberBadgeManager.class);

    private final ActivityPointService activityPointService;
    private final BadgeService badgeService;
    private final MemberBadgeMapper memberBadgeMapper;
    private final UserActivityRecordMapper userActivityRecordMapper;

    @Autowired
    public MemberBadgeManager(ActivityPointService activityPointService, BadgeService badgeService, 
                              MemberBadgeMapper memberBadgeMapper, 
                              UserActivityRecordMapper userActivityRecordMapper) {
        this.activityPointService = activityPointService;
        this.badgeService = badgeService;
        this.memberBadgeMapper = memberBadgeMapper;
        this.userActivityRecordMapper = userActivityRecordMapper;
    }

    // 활동을 처리하고 포인트와 뱃지를 업데이트하는 메서드
    @Transactional
    public void processActivity(int memberId, String activityType) {
        // 활동에 대한 포인트 계산
        float points = activityPointService.calculateActivityPoints(activityType);
        
        // 활동 기록 생성
        UserActivityRecordRequestDTO activityRecord = new UserActivityRecordRequestDTO();
        activityRecord.setActivityType(activityType);
        activityRecord.setMemberId(memberId);
        
        // 활동 기록을 데이터베이스에 삽입
        userActivityRecordMapper.insertActivity(activityRecord);
        
        // 멤버의 현재 점수 업데이트
        updateMemberBadgePoints(memberId, points);
    }

    private void updateMemberBadgePoints(int memberId, float points) {
        // 현재 점수 조회
        BigDecimal currentPoints = badgeService.getCurrentPoints(memberId);
        float totalPoints = currentPoints.floatValue() + points;

        // MemberBadge의 current_points 업데이트
        MemberBadgeRequestDTO badgeRequest = new MemberBadgeRequestDTO();
        badgeRequest.setMember_id(memberId);
        badgeRequest.setCurrent_points(totalPoints); // 현재 점수 업데이트
        badgeRequest.setBadge_level(badgeService.getBadgeLevel(BigDecimal.valueOf(totalPoints))); // 뱃지 레벨 업데이트
        memberBadgeMapper.updateBadge(badgeRequest);
    }
}
