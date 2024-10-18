package com.multipjt.multi_pjt.record.exercise.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ExerResponseDTO {
 
    private Long exerciseId;
    private LocalDate recordDate;
    private String exerciseType;
    private int duration;
    private float calories;
    private String intensity;
    private float distance;
    private int weight;
    private Long memberId;

}
