package com.multipjt.multi_pjt.record.exercise.domain;

import lombok.Data;

@Data
public class SetResponseDTO {

    private Long set_id;
    private Float distance;
    private Integer weight;
    private Integer repetitions;
    private Long exercise_id;

}
