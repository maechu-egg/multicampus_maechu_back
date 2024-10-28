package com.multipjt.multi_pjt.crew.domain.battle;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewBattleRequestDTO {
    private int battle_id;
    private String battle_name;
    private String battle_goal;
    private String battle_end_recruitment;
    private String battle_end_date;
    private String battle_content;
    private int battle_state;
    private int crew_id;
}
