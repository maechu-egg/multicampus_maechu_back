package com.multipjt.multi_pjt.record.diet.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DietResponseDTO {
    
    private Long dietid;
    private LocalDate recordDate;
    private String mealType; // enum String으로 가져옴
    private Long memberId;

}
