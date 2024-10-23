package com.multipjt.multi_pjt.badge.service;

import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.badge.dao.MemberBadgeMapper;
import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeResponseDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordRequsetDTO;

@Service
public class MemberBadgeManager {

    private final ActivityPointService activityPointService;
    private final BadgeService badgeService;
    private final MemberBadgeMapper memberBadgeMapper;
    private final UserActivityRecordMapper userActivityRecordMapper;

    public MemberBadgeManager(ActivityPointService activityPointService, BadgeService badgeService, MemberBadgeMapper memberBadgeMapper, UserActivityRecordMapper userActivityRecordMapper) {
        this.activityPointService = activityPointService;
        this.badgeService = badgeService;
        this.memberBadgeMapper = memberBadgeMapper;
        this.userActivityRecordMapper = userActivityRecordMapper;
    }

    // 활동을 처리하고 포인트와 뱃지를 업데이트하는 메서드
    public void processActivity(int memberId, String activityType) {
        // 활동에 따른 포인트 계산
        float points = activityPointService.calculateActivityPoints(activityType);
    
        // 포인트가 0이면 잘못된 활동 유형이므로 종료
        if (points == 0) {
            return;
        }

        // 오늘 이미 해당 활동이 이루어졌는지 확인 (1일 1회만 포인트 획득 가능)
        if (userActivityRecordMapper.countTodayActivity(memberId, activityType) > 0) {
            return; // 이미 오늘 포인트가 적립된 경우 아무 작업도 하지 않음
        }

        // 현재 회원의 뱃지 정보 가져오기
        MemberBadgeResponseDTO currentBadge = memberBadgeMapper.getselectBadgeByMemberId(memberId);

        // 새로운 활동 기록 추가
        UserActivityRecordRequsetDTO newRecord = new UserActivityRecordRequsetDTO();
        newRecord.setMember_id(memberId);
        newRecord.setActivity_type(activityType);
        newRecord.setPoints(points);
        userActivityRecordMapper.insertActivity(newRecord);

        // 뱃지가 없으면 새로 생성
        if (currentBadge == null) {
            MemberBadgeRequestDTO newBadge = new MemberBadgeRequestDTO();
            newBadge.setMember_id(memberId);
            newBadge.setCurrent_points(points);
            newBadge.setBadge_level(badgeService.getBadgeLevel(points)); // 포인트에 따른 뱃지 레벨 설정
            memberBadgeMapper.insertBadge(newBadge);
        } else {
            // 기존 뱃지가 있을 경우 포인트 업데이트
            float updatedPoints = currentBadge.getCurrent_points() + points; // 기존 포인트에 새로운 포인트 추가
            String newBadgeLevel = badgeService.getBadgeLevel(updatedPoints); // 새로 계산된 포인트로 배지 레벨 확인

            // 뱃지 업데이트
            currentBadge.setCurrent_points(updatedPoints);
            currentBadge.setBadge_level(newBadgeLevel); // 새로운 배지 레벨 설정

            MemberBadgeRequestDTO updatedBadge = new MemberBadgeRequestDTO();
            updatedBadge.setBadge_id(currentBadge.getBadge_id());
            updatedBadge.setCurrent_points(currentBadge.getCurrent_points());
            updatedBadge.setBadge_level(currentBadge.getBadge_level());
            updatedBadge.setMember_id(currentBadge.getMember_id());

            memberBadgeMapper.updateBadge(updatedBadge); // 배지 업데이트
        }
    }
}
