package com.multipjt.multi_pjt.badge.service;

import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeResponseDTO;

public class BadgeService {

    // 현재 포인트를 기반으로 뱃지 등급을 계산하는 메소드
    public String getBadgeLevel(float currentPoints) {
        if (currentPoints >= 100) {
            return "Diamond";
        } else if (currentPoints >= 70) {
            return "Platinum";
        } else if (currentPoints >= 50) {
            return "Gold";
        } else if (currentPoints >= 30) {
            return "Silver";
        } else if (currentPoints >= 10) {
            return "Bronze";
        } else {
            return "No Badge";
        }
    }

    // 새로운 활동 포인트를 적용하여 뱃지를 업데이트하는 메소드
    public void updateBadge(MemberBadgeResponseDTO badge, float newPoints) {
        // 새로운 포인트 추가
        float updatedPoints = badge.getCurrent_points() + newPoints;

        // 새로운 포인트로 뱃지 등급 계산
        String newBadgeLevel = getBadgeLevel(updatedPoints);

        // 뱃지 업데이트
        badge.setCurrent_points(updatedPoints);
        badge.setBadge_level(newBadgeLevel);
    }
}
