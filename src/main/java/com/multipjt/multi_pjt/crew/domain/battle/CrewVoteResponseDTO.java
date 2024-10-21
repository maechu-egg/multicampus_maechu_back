package com.multipjt.multi_pjt.crew.domain.battle;

import lombok.Data;

@Data
public class CrewVoteResponseDTO {
    private int vote_id;
    private int battle_id;
    private int participant_id;
    private int member_id;
}
