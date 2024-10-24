package com.multipjt.multi_pjt.badge.service;

import org.springframework.stereotype.Service;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeResponseDTO;

@Service
public class BadgeService {

    // 현재 포인트를 기반으로 뱃지 등급을 계산하는 메소드
    public String getBadgeLevel(float currentPoints) {
        if (currentPoints >= 100) return "Diamond";
        if (currentPoints >= 70) return "Platinum";
        if (currentPoints >= 50) return "Gold";
        if (currentPoints >= 30) return "Silver";
        if (currentPoints >= 10) return "Bronze";
        return "No Badge";
    }

    // 새로운 활동 포인트를 적용하여 뱃지를 업데이트하는 메소드
    public void updateBadge(MemberBadgeResponseDTO badge, float newPoints) {
        // 새로운 포인트 추가 및 뱃지 등급 계산
        badge.setCurrent_points(badge.getCurrent_points() + newPoints);
        badge.setBadge_level(getBadgeLevel(badge.getCurrent_points()));
    }
}
