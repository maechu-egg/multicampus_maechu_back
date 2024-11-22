package com.multipjt.multi_pjt.badge.domain.badge;

import lombok.Data;

@Data
public class CrewBadgeRequestDTO {
    private int crew_badge_id;
    private float crew_current_points;
    private int member_id;
    private String badge_level;
    private int crew_battle_wins;

    public void setCrew_battle_wins(int crew_battle_wins) {
        this.crew_battle_wins = crew_battle_wins;
    }
}
