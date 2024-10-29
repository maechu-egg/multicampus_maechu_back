package com.multipjt.multi_pjt.record.exercise.domain;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ExerRequestDTO {
    private Long exercise_id; // exerInsert 후 exercise_id 값 반환 받기 위해 설정
    private String exercise_type;
    private Integer duration; 
    private Float calories;
    @Enumerated(EnumType.STRING)
    private Intensity intensity; 
    private Long member_id;
}
