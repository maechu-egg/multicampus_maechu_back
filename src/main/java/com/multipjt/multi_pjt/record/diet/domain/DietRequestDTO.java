package com.multipjt.multi_pjt.record.diet.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class DietRequestDTO {
    
    // dietId는 auto increment 여서 지정하지 않음, date도 default 값을 기입할 거여서 기입 x 
    private Integer member_id;
    @Enumerated(EnumType.STRING)
    private MealType meal_type;
    // recordDate는 default 

}