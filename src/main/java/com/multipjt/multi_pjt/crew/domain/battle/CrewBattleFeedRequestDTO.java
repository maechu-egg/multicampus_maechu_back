package com.multipjt.multi_pjt.crew.domain.battle;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewBattleFeedRequestDTO {
    private int feed_id;
    private String feed_img;
    private String feed_post;
    private int feed_kcal;
    private String feed_sport;
    private int feed_exTime;
    private int participant_id;
    private int battle_id;
    private int member_id;
}
