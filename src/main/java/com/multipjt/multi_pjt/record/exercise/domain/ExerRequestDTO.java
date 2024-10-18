package com.multipjt.multi_pjt.record.exercise.domain;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ExerRequestDTO {
    private String exerciseType;
    private int duration; 
    private float calories; // default 값 존재
    @Enumerated(EnumType.STRING)
    private Intensity intensity; 
    private float distance; // default 값 존재
    private int weight; // default 값 존재
    private Long memberId;
}
