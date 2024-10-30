package com.multipjt.multi_pjt.crew.domain.battle;

import lombok.Data;

@Data
public class BattleMemberResponseDTO {
    private Integer participant_id;
    private Integer battle_id;
    private Integer member_id;
    private String  nickname;
    private String  profile_region;
    private Integer profile_age;
    private Integer total_feed_kcal;
    private Integer total_feed_exTime;
    private Integer feed_count;
    private String badge_level;
}
