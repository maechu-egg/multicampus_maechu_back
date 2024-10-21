package com.multipjt.multi_pjt.crew.domain.battle;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrewVoteRequestDTO {
    private int vote_id;
    private int battle_id;
    private int participant_id;
    private int member_id;
}
