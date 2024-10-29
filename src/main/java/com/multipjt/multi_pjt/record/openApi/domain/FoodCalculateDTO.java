package com.multipjt.multi_pjt.record.openApi.domain;

import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;

import lombok.Data;

@Data
public class FoodCalculateDTO {
    private String foodNm;          // 선택한 식품명
    private Integer inputQuantity;   // 사용자가 입력한 그램수
    private Integer originalQuantity; // API에서 제공하는 기준 그램수
    private Double energy;
    private Double carbs;
    private Double protein;
    private Double fat;
    
    // 영양소 계산 메서드
    public ItemRequestDTO calculateNutrients() {
        double ratio = inputQuantity.doubleValue() / originalQuantity.doubleValue();
        
        ItemRequestDTO item = new ItemRequestDTO();
        item.setItem_name(foodNm);
        item.setQuantity(inputQuantity);
        item.setCalories((int) Math.round(energy * ratio));
        item.setCarbs((int) Math.round(carbs * ratio));
        item.setProtein((int) Math.round(protein * ratio));
        item.setFat((int) Math.round(fat * ratio));
        
        return item;
    }
}