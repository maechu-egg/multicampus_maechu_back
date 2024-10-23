package com.multipjt.multi_pjt.record.exercise.domain;

import lombok.Data;

@Data
public class SetRequestDTO {
    
    //private Long setId >> auto increment
    private Float distance;
    private Integer weight;
    private Integer repetitions;
    private Long exercise_id;
    
}
