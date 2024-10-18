package com.multipjt.multi_pjt.record.exercise.domain;

import java.time.LocalDate;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ExerRequestDTO {
    private LocalDate recorDate; // default 값 존재
    private String exerciseType;
    private int duration; // default 값 존재
    private float calories; // default 값 존재
    @Enumerated(EnumType.STRING)
    private Intensity intensity; 
    private float distance; // default 값 존재
    private int weight; // default 값 존재
    private Long memberId;
}
