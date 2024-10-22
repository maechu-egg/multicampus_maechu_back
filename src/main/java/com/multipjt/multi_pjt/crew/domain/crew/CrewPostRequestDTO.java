package com.multipjt.multi_pjt.crew.domain.crew;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private int member_id;
}
