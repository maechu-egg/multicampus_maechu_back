package com.multipjt.multi_pjt.crew.domain.crew;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrewMemberRequestDTO {
    private int crew_id;
    private int member_id;
    private int battle_wins;
    private int crew_member_state;
}
