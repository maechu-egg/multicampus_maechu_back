package com.multipjt.multi_pjt.badge.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IBadgeService {
    // 특정 회원의 현재 점수를 조회하는 메서드
    BigDecimal getCurrentPoints(Long memberId);
    
    // 주어진 점수에 따라 회원의 뱃지 레벨을 결정하는 메서드
    String getBadgeLevel(BigDecimal currentPoints);
    
    // 주어진 점수에 따라 다음 뱃지까지 필요한 점수를 계산하는 메서드
    BigDecimal getPointsToNextBadge(BigDecimal currentPoints);
    
    // 특정 회원의 점수를 업데이트하는 메서드
    void updateUserPoints(Long memberId, BigDecimal points);
    
    // 모든 회원의 점수 순위를 조회하는 메서드
    List<Map<String, Object>> getUserRankings();
    
    // 특정 회원의 모든 활동을 처리하는 메서드
    void processUserActivities(Long memberId);
    
    // 특정 회원의 뱃지를 생성하는 메서드
    void createBadge(Long memberId);
}