package com.multipjt.multi_pjt.badge.service;

import com.multipjt.multi_pjt.badge.dao.MemberBadgeMapper;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeResponseDTO;

public class MemberBadgeManager {

    private ActivityPointService activityPointService;
    private BadgeService badgeService;
    private MemberBadgeMapper memberBadgeMapper;

    public MemberBadgeManager(ActivityPointService activityPointService, BadgeService badgeService, MemberBadgeMapper memberBadgeMapper) {
        this.activityPointService = activityPointService;
        this.badgeService = badgeService;
        this.memberBadgeMapper = memberBadgeMapper;
    }

    // 활동을 처리하고 뱃지를 업데이트하는 메소드
    public void processActivity(int memberId, String activityType) {
        // 활동에 따른 포인트 계산
        float points = activityPointService.calculateActivityPoints(activityType);

        // 현재 회원의 뱃지 정보 가져오기
        MemberBadgeResponseDTO currentBadge = memberBadgeMapper.getselectBadgeByMemberId(memberId);

        // 뱃지가 없으면 새로 생성
        if (currentBadge == null) {
            MemberBadgeRequestDTO newBadge = new MemberBadgeRequestDTO();
            newBadge.setMember_id(memberId);
            newBadge.setCurrent_points(points);
            newBadge.setBadge_level(badgeService.getBadgeLevel(points));
            memberBadgeMapper.insertBadge(newBadge);
        } else {
            // 포인트와 뱃지 등급 업데이트
            badgeService.updateBadge(currentBadge, points);
            MemberBadgeRequestDTO updatedBadge = new MemberBadgeRequestDTO();
            updatedBadge.setBadge_id(currentBadge.getBadge_id());
            updatedBadge.setCurrent_points(currentBadge.getCurrent_points());
            updatedBadge.setBadge_level(currentBadge.getBadge_level());
            updatedBadge.setMember_id(currentBadge.getMember_id());
            memberBadgeMapper.updateBadge(updatedBadge);
        }
    }
}
