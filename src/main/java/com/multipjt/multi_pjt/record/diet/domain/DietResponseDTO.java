package com.multipjt.multi_pjt.record.diet.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DietResponseDTO {
    
    private Integer diet_id;
    private LocalDate record_date;
    private String meal_type; // enum String으로 가져옴
    private Integer member_id;

}