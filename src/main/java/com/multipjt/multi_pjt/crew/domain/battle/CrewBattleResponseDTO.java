package com.multipjt.multi_pjt.crew.domain.battle;

import lombok.Data;
import java.time.LocalDateTime;
@Data
public class CrewBattleResponseDTO {
    private int battle_id;
    private String battle_name;
    private String battle_goal;
    private LocalDateTime battle_end_recruitment;
    private LocalDateTime battle_end_date;
    private String battle_content;
    private int battle_state;
    private int crew_id;
}