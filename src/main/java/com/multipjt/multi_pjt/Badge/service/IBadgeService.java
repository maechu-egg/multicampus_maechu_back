package com.multipjt.multi_pjt.badge.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IBadgeService {
    BigDecimal getCurrentPoints(Long memberId);
    String getBadgeLevel(BigDecimal currentPoints);
    BigDecimal getPointsToNextBadge(BigDecimal currentPoints);
    void updateUserPoints(Long memberId, BigDecimal points);
    List<Map<String, Object>> getUserRankings();
    void processUserActivities(Long memberId);
    void createBadge(Long memberId);
}