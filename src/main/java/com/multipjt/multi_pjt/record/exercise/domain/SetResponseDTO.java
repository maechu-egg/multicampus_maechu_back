package com.multipjt.multi_pjt.record.exercise.domain;

import lombok.Data;

@Data
public class SetResponseDTO {

    private Integer set_id;
    private Double distance;
    private Integer weight;
    private Integer repetitions;
    private Integer exercise_id;

}
