package com.multipjt.multi_pjt.crew.domain.crew;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewMemberRequestDTO {
    private int crew_id;
    private int member_id;
    private int battle_wins;
    private int crew_member_state;
}
