package com.multipjt.multi_pjt.record.exercise.domain;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ExerRequestDTO {
    private String exercise_type;
    private Integer duration; 
    private Float calories;
    @Enumerated(EnumType.STRING)
    private Intensity intensity; 
    private Long member_id;
}
