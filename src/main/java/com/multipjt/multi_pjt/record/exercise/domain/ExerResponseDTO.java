package com.multipjt.multi_pjt.record.exercise.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ExerResponseDTO {
 
    private Integer exercise_id;
    private LocalDate record_date;
    private String exercise_type;
    private Integer duration;
    private Integer calories;
    private String intensity;
    private Integer member_id;
    private Double met;
}
