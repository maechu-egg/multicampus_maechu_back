package com.multipjt.multi_pjt.crew.domain.crew;

import lombok.Data;

@Data
public class CrewMemberRequestDTO {
    private int crew_id;
    private String member_id;
    private int battle_wins;
    private int crew_member_state;
}
