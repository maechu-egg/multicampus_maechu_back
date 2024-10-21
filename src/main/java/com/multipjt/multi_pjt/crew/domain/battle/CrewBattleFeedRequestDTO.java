package com.multipjt.multi_pjt.crew.domain.battle;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrewBattleFeedRequestDTO {
    private int feed_id;
    private String feed_img;
    private String feed_post;
    private int feed_kcal;
    private String feed_sport;
    private int feed_time;
    private int participant_id;
}
