package com.multipjt.multi_pjt.crew.domain.crew;

import lombok.Data;

@Data
public class CrewCommentsResponseDTO {
    private int crew_comments_id;
    private String crew_comments_content;
    private String crew_comments_date;
    private int crew_post_id;
    private int member_id;
}