package com.multipjt.multi_pjt.crew.domain.battle;

import lombok.Data;

@Data
public class CrewBattleFeedResponseDTO {
    private int feed_id;
    private String feed_img;
    private String feed_post;
    private int feed_kcal;
    private String feed_sport;
    private int feed_time;
    private int participant_id;
}
