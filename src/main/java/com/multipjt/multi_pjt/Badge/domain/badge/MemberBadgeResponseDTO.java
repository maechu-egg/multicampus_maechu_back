package com.multipjt.multi_pjt.Badge.domain.badge;

import lombok.Data;

@Data
public class MemberBadgeResponseDTO {
    private int badge_id;
    private float current_points;
    private int member_id;
    private String badge_level;
}
