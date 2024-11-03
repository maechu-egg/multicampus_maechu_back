package com.multipjt.multi_pjt.record.exercise.domain;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ExerRequestDTO {
    private Integer exercise_id; // exerInsert 후 exercise_id 값 반환 받기 위해 설정
    private String exercise_type;
    private Integer duration; 
    private Integer calories;
    @Enumerated(EnumType.STRING)
    private Intensity intensity; 
    private Integer member_id;
    private Double met;
}
