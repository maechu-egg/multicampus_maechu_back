package com.multipjt.multi_pjt.record.summary.ctrl;

import com.multipjt.multi_pjt.record.diet.service.DietService;
import com.multipjt.multi_pjt.record.exercise.service.ExerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("record/summary")
public class SummaryControl {

    @Autowired
    private ExerService exerService;

    @Autowired
    private DietService dietService;


    @GetMapping("/daily")
    public ResponseEntity<Map<String, Object>> getDailySummary(
            @RequestBody @Validated Map<String, Object> params ) {
    
        System.out.println("class endPoint >> " + "/record/summary/daily");
    
        if(params.get("member_id") == null){
            System.out.println("member_id 없음");
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        } else if(params.get("record_date") == null){
            System.out.println("record_date 없음");
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

        try {
            // 1. TDEE와 권장 영양소 계산
            Map<String, Object> recommendedNutrients = dietService.calculateTdeeRow((Integer)params.get("member_id"));
            System.out.println("debug >>> recommendedNutrients " + recommendedNutrients);
            System.out.println("TDEE 계산 완료");
            
            // 2. 오늘 섭취한 영양소 조회
            List<Map<String, Object>> consumedNutrients = dietService.mealNutCheckRow(params);
            System.out.println("debug >>> consumedNutrients " + consumedNutrients);
            System.out.println("섭취한 영양소 조회 완료");

            // 3. 오늘 소모한 칼로리 조회
            List<Map<String, Object>> burnedCalories = exerService.exerCaloriesGetRow(params);
            System.out.println("debug >>> burnedCalories " + burnedCalories);
            System.out.println("소모한 칼로리 조회 완료");
            // 4. 결과 종합
            Map<String, Object> summary = new HashMap<>();
            System.out.println("debug >>> summary " + summary);
            System.out.println("결과 종합 시작");
            // 권장 영양소 정보
            summary.put("recommended", recommendedNutrients);
            System.out.println("debug >>> summary " + summary);
            System.out.println("권장 영양소 정보 추가 완료");
            // 섭취한 영양소 총합 계산
            int totalCalorie = 0;
            int totalProtein = 0;
            int totalFat = 0;
            int totalCarb = 0;
            int totalQuantity = 0;
            if (consumedNutrients != null) {
                for (Map<String, Object> meal : consumedNutrients) {
                    Object calories = meal.get("totalCalorie");
                    Object protein = meal.get("totalProtein");
                    Object fat = meal.get("totalFat");
                    Object carb = meal.get("totalCarb");
                    Object quantity = meal.get("totalQuantity");

                    if (calories != null) totalCalorie += ((Number)calories).intValue();
                    if (protein != null) totalProtein += ((Number)protein).intValue();
                    if (fat != null) totalFat += ((Number)fat).intValue();
                    if (carb != null) totalCarb += ((Number)carb).intValue();
                    if (quantity != null) totalQuantity += ((Number)quantity).intValue();
                }
            }
            System.out.println("debug >>> totalCalorie " + totalCalorie);
            System.out.println("debug >>> totalProtein " + totalProtein);
            System.out.println("debug >>> totalFat " + totalFat);
            System.out.println("debug >>> totalCarb " + totalCarb);
            System.out.println("debug >>> totalQuantity " + totalQuantity);
            System.out.println("섭취한 영양소 총합 계산 완료");
            Map<String, Object> consumed = new HashMap<>();
            consumed.put("calorie", totalCalorie);
            consumed.put("protein", totalProtein);
            consumed.put("fat", totalFat);
            consumed.put("carb", totalCarb);
            consumed.put("quantity", totalQuantity);
            // 영양소 비율 계산 시작
            if (totalCalorie > 0) {
                // 각 영양소가 기여하는 칼로리 계산
                double proteinCalories = totalProtein * 4.0;
                double fatCalories = totalFat * 9.0;
                double carbCalories = totalCarb * 4.0;
                
                // 비율 계산 (백분율)
                consumed.put("proteinRatio", (int)((proteinCalories / totalCalorie) * 100));
                consumed.put("fatRatio", (int)((fatCalories / totalCalorie) * 100));
                consumed.put("carbRatio", (int)((carbCalories / totalCalorie) * 100));
            }
            System.out.println("debug >>> consumed " + consumed);
            System.out.println("영양소 비율 계산 완료");
            summary.put("consumed", consumed);
            System.out.println("debug >>> summary " + summary);
            System.out.println("섭취한 영양소 총합 정보 추가 완료");
            // 소모한 칼로리 계산
            int totalBurnedCalories = 0;
            if (burnedCalories != null) {
                for (Map<String, Object> exercise : burnedCalories) {
                    totalBurnedCalories += Integer.parseInt(exercise.get("totalCalories").toString());
                }
            }
            System.out.println("debug >>> totalBurnedCalories " + totalBurnedCalories);
            System.out.println("소모한 칼로리 계산 완료");
            summary.put("burnedCalories", totalBurnedCalories);
            System.out.println("debug >>> summary " + summary);
            System.out.println("소모한 칼로리 정보 추가 완료");
            
            return new ResponseEntity<>(summary, HttpStatus.OK);
            
        } catch (Exception e) {
            System.out.println("서버 오류 발생: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 