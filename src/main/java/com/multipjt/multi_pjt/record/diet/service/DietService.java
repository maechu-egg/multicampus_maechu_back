package com.multipjt.multi_pjt.record.diet.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.record.diet.dao.DietMapper;
import com.multipjt.multi_pjt.record.diet.domain.DietRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.DietResponseDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemResponseDTO;

@Service
public class DietService {

    @Autowired
    private DietMapper dietMapper;

    public int dietInsertRow(Map<String,Object> map){
        return dietMapper.dietInsert(map);
    } 


    public int itemInsertRow(ItemRequestDTO itemRequestDTO){
        return dietMapper.itemInsert(itemRequestDTO);
    }

    public Integer findDietRow(Map<String,Object> map){
        return dietMapper.findDietNumber(map);
    }

    public List<DietResponseDTO> dietFindAllRow(Map<String,Object> map){
        return dietMapper.dietFindAll(map);
    }
        
    public List<ItemResponseDTO> itemFindAllRow(Integer diet_id){
        return dietMapper.itemFindAll(diet_id);
    }

    public int itemUpdateRow(ItemRequestDTO itemRequestDTO){
        return dietMapper.itemUpdate(itemRequestDTO);
    }

    public int itemDeleteRow(Integer item_id){
        return dietMapper.itemDelete(item_id);
    }

    public int deleteRecordRow(Integer diet_id){
        return dietMapper.dietDelete(diet_id);
    }

    public List<Map<String,Object>> mealNutCheckRow(Map<String,Object> map){
        return dietMapper.mealNutCheck(map);
    }

    // public Map<String,String> getMemberInfoRow(Integer memberId){
    //     return dietMapper.getMemberInfo(memberId);
    // }

    public int mealUpdateRow(DietRequestDTO dietRequestDTO){
        return dietMapper.mealUpdate(dietRequestDTO);
    }
    public List<Map<String,Object>> getMonthDietRow(Map<String,Object> map){
        return dietMapper.getMonthDiet(map);
    }

//    public Map<String,Object> memberDataGetRow(Integer memberId){
//        return dietMapper.memberDataGet(memberId);
//    }

    public Map<String,Object> calculateTdeeRow(Integer memberId){
        Map<String,Object> info = dietMapper.memberDataGet(memberId);
        // 기초대사량(BMR) 계산 - 해리스-베네딕트 공식 사용
        Integer bmr;
        if (info.get("profile_gender").equals("M")) {
            bmr = (int) Math.round(88.362 + (13.397 * Double.parseDouble(info.get("profile_weight").toString()))
                        + (4.799 * Double.parseDouble(info.get("profile_height").toString()))
                        - (5.677 * Double.parseDouble(info.get("profile_age").toString())));
        } else {
            bmr = (int) Math.round(447.593 + (9.247 * Double.parseDouble(info.get("profile_weight").toString()))
                        + (3.098 * Double.parseDouble(info.get("profile_height").toString()))
                        - (4.330 * Double.parseDouble(info.get("profile_age").toString())));
        }

        // 활동레벨에 따른 TDEE 계산
        double activityMultiplier;
        switch (Integer.parseInt(info.get("profile_workout_frequency").toString())) {
            case 0 :
                activityMultiplier = 1.2;     // 좌식생활
                break;
            case 1 :
                activityMultiplier = 1.375;   // 가벼운 운동 (주1-3회)
                break;
            case 2 :
                activityMultiplier = 1.55;    // 중간 강도 운동 (주3-5회)
                break;
            case 3 :
                activityMultiplier = 1.725;   // 활동적인 운동 (주6-7회)
                break;
            case 4 :
                activityMultiplier = 1.9;     // 매우 활동적 (하루 2회 운동)
                break;
            default:
                throw new IllegalArgumentException("잘못된 활동 레벨입니다");
        }

        Integer tdee = (int) Math.round(bmr * activityMultiplier);
        Integer recommendedCalories = tdee;
        Integer recommendedProtein;
        Integer recommendedFat;
        Integer recommendedCarb;        
        
        Double proteinRate;
        Double fatRate;
        Double carbRate;        

        // 다이어트 목표에 따른 칼로리 조정
    switch (info.get("profile_goal").toString().toLowerCase()) {
        case "다이어트":
            recommendedCalories = (int) Math.round(tdee * 0.75);

            carbRate = 0.3;
            proteinRate = 0.5;
            fatRate = 0.2;

            recommendedCarb = (int) Math.round(recommendedCalories * carbRate) / 4;
            recommendedProtein = (int) Math.round(recommendedCalories * proteinRate) / 4;
            recommendedFat = (int) Math.round(recommendedCalories * fatRate) / 9;
            break;
        case "벌크업":
            recommendedCalories = (int) Math.round(tdee * 1.1);
            carbRate = 0.6;
            proteinRate = 0.3;
            fatRate = 0.1;

            recommendedCarb = (int) Math.round(recommendedCalories * carbRate) / 4;
            recommendedProtein = (int) Math.round(recommendedCalories * proteinRate) / 4;
            recommendedFat = (int) Math.round(recommendedCalories * fatRate) / 9;
            break;
        case "린매스업":
            recommendedCalories = (int) Math.round(tdee * 1.05);
            carbRate = 0.4;
            proteinRate = 0.4;
            fatRate = 0.2;

            recommendedCarb = (int) Math.round(recommendedCalories * carbRate) / 4;
            recommendedProtein = (int) Math.round(recommendedCalories * proteinRate) / 4;
            recommendedFat = (int) Math.round(recommendedCalories * fatRate) / 9;
            break;
        case "유지":
            recommendedCalories = tdee;
            carbRate = 0.5;
            proteinRate = 0.3;
            fatRate = 0.2;

            recommendedCarb = (int) Math.round(recommendedCalories * carbRate) / 4;
            recommendedProtein = (int) Math.round(recommendedCalories * proteinRate) / 4;
            recommendedFat = (int) Math.round(recommendedCalories * fatRate) / 9;
            break;
        default:
            throw new IllegalArgumentException("잘못된 다이어트 목표입니다");
        }

        Map<String,Object> result = new HashMap<>();
        result.put("bmr", bmr);
        result.put("tdee", tdee);
        result.put("recommendedCalories", recommendedCalories);
        result.put("recommendedProtein", recommendedProtein);
        result.put("recommendedFat", recommendedFat);
        result.put("recommendedCarb", recommendedCarb);
        result.put("weight", Double.parseDouble(info.get("profile_weight").toString()));
        result.put("goal", info.get("profile_goal").toString());
        result.put("proteinRate", proteinRate);
        result.put("fatRate", fatRate);
        result.put("carbRate", carbRate);
        return result;
    }
}
