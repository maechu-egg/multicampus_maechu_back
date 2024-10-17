package com.multipjt.multi_pjt.record.diet.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DietResponseDTO {
    
    private Long dietid;
    private LocalDate recordDate; // 시간은 필요 없어서 sql에서 받아올 때 시간은 없애고 받아옴
    private String mealType; // enum String으로 가져옴
    private Long memberId;

}
