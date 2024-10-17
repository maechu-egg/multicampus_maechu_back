package com.multipjt.multi_pjt.record.diet.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import java.time.LocalDate;


@Data
public class DietRequestDTO {
    
    // dietId는 auto increment 여서 지정하지 않음, date도 default 값을 기입할 거여서 기입 x 
    private Long memberId;
    @Enumerated(EnumType.STRING)
    private MealType mealType;
    private LocalDate recordDate;

}
