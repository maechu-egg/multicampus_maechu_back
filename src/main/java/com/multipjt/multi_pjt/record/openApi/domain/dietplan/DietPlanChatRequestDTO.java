package com.multipjt.multi_pjt.record.openApi.domain.dietplan;

import java.util.List;
import lombok.Data;

@Data
public class DietPlanChatRequestDTO {
    private int calories; // 칼로리
    private String exercisegoal; // 운동목표
    private String ingredients; // 재료
    private List<String> dietaryRestrictions; // 식이 제한
    private List<String> allergies; // 알레르기
    private List<String> medicalConditions; // 의학적 조건
    private String mealsPerDay; // 하루 식사 수
    private List<String> cookingPreference; // 요리 선호도
    public String getExercisegoal;
}
