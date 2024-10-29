package com.multipjt.multi_pjt.crew.domain.crew;

import lombok.Data;

@Data
public class CrewMemberResponseDTO {
    private int crew_id;
    private int member_id;
    private String nickname;
    private String profile_region;
    private int profile_age;
    private int battle_wins;
    private int crew_member_state;
}
