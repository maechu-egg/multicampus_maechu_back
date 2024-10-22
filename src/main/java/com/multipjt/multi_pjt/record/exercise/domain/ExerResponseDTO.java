package com.multipjt.multi_pjt.record.exercise.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ExerResponseDTO {
 
    private Long exercise_id;
    private LocalDate record_date;
    private String exercise_type;
    private Integer duration;
    private Float calories;
    private String intensity;
    private Long member_id;
}
