package com.multipjt.multi_pjt.crew.domain.crew;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrewCommentsRequestDTO {
    private int crew_comments_id;
    private String crew_comments_content;
    private String crew_comments_date;
    private int crew_post_id;
    private int member_id;
}
