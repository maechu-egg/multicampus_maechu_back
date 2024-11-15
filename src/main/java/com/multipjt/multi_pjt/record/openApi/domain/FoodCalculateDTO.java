package com.multipjt.multi_pjt.record.openApi.domain;

import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;

import lombok.Data;

@Data
public class FoodCalculateDTO {
    private String foodNm;          // 선택한 식품명
    private Integer inputQuantity;   // 사용자가 입력한 그램수
    private Double energy;
    private Double carbs;
    private Double protein;
    private Double fat;
    private Integer diet_id;
    
    // 영양소 계산 메서드
    public ItemRequestDTO calculateNutrients() {
        // 사용자가 입력한 그램수와 API에서 제공하는 기준 그램수의 비율 계산
        double ratio = inputQuantity.doubleValue() / 100;

        // 영양소 계산
        ItemRequestDTO item = new ItemRequestDTO();
        item.setItem_name(foodNm);
        item.setQuantity(inputQuantity);
        item.setCalories((int) Math.round(energy * ratio));
        item.setCarbs((int) Math.round(carbs * ratio));
        item.setProtein((int) Math.round(protein * ratio));
        item.setFat((int) Math.round(fat * ratio));
        item.setDiet_id(diet_id);
        return item;
    }
}
