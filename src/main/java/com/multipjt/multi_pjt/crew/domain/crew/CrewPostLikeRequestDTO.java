package com.multipjt.multi_pjt.crew.domain.crew;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewPostLikeRequestDTO {
    private int like_id;
    private int crew_post_id;
    private int member_id;
    private int crew_id;
}
