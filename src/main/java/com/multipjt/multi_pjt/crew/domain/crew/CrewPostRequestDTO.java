package com.multipjt.multi_pjt.crew.domain.crew;

import lombok.Data;

@Data
public class CrewPostRequestDTO {
    private int crew_post_id;
    private String crew_post_title;
    private String crew_post_content;
    private String crew_post_img;
    private int crew_post_like;
    private int crew_post_hits;
    private int crew_post_state;
    private String crew_post_date;
    private int crew_id;
    private String member_id;
}
