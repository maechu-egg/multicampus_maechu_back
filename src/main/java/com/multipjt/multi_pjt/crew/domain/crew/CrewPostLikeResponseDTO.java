package com.multipjt.multi_pjt.crew.domain.crew;

import lombok.Data;

@Data
public class CrewPostLikeResponseDTO {
    private int like_id;
    private int crew_post_id;
    private int member_id;
    private int crew_id;

}
