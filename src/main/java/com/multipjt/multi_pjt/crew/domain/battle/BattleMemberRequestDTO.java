package com.multipjt.multi_pjt.crew.domain.battle;

import lombok.Data;

@Data
public class BattleMemberRequestDTO {
    private int participant_id;
    private int battle_id;
    private String member_id;
}
