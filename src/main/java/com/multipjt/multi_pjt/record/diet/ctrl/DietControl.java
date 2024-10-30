package com.multipjt.multi_pjt.record.diet.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.multipjt.multi_pjt.record.diet.domain.DietRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemResponseDTO;
import com.multipjt.multi_pjt.record.diet.service.DietService;
import com.multipjt.multi_pjt.record.openApi.domain.FoodCalculateDTO;
import com.multipjt.multi_pjt.record.openApi.service.ApiService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("record/diet")
public class DietControl {
    
    @Autowired
    private DietService dietService;


    //규칙 : 한 끼에 같은 식품 중복 작성 불가, 하루에 끼니 유형은 하나씩만 가능
    // 식단 수정 기능은 넣지 않음, 식단 기입 여부는 식품 기록 여부에 따라 결정


    // 식단 추가
    @PostMapping("/insert/meal")
    public ResponseEntity<Integer> mealInsert(@RequestBody DietRequestDTO dietRequestDTO) {
        System.out.println("class endPoint >> " + "/diet/insert/record");
        int result = dietService.dietInsertRow(dietRequestDTO);
        System.out.println("result >>" + result);
        if(result == 1){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }
    // 식품 추가
    @PostMapping("/insert/item")
    public ResponseEntity<Integer> itemInsert(@RequestBody ItemRequestDTO itemRequestDTO) {
        System.out.println("class endPoint >> " + "/diet/insert/item");
        int result = dietService.itemInsertRow(itemRequestDTO);
        System.out.println("result >>" + result);
        if(result == 1){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }
    
    // 식단 번호 찾기
    @GetMapping("/get/dietnumber")
    public ResponseEntity<Long> findDietNumber(@RequestBody Map<String,Object> map) {
        System.out.println("class endPoint >> " + "/diet/insert/record");
        Long result = dietService.findDietRow(map);
        System.out.println("result >>" + result);
        if(result != 0){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }

    // 특정 식단 식품 모두 찾기
    @GetMapping("/get/items")
    public ResponseEntity<List<ItemResponseDTO>> findDietItems(@RequestParam(name = "diet_id") Long diet_id) {
        System.out.println("class endPoint >> " + "/diet/get/items");
        List<ItemResponseDTO> result = dietService.itemFindAllRow(diet_id);
        System.out.println("result >>" + result);
        if(result != null){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }

    // 식품 수정, 사용자는 양만 변경 가능, 그러면 영양성분 또한 그대로 반영, 식품은 변경 불가능
    @PutMapping("/update/item")
    public ResponseEntity<Integer> itemUpdate(@RequestBody ItemRequestDTO itemRequestDTO) {
        System.out.println("class endPoint >> " + "/diet/update/item");
        int result = dietService.itemUpdateRow(itemRequestDTO);
        System.out.println("result >>" + result);
        if(result == 1){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }

    // 식품 삭제
    @DeleteMapping("/delete/item")
    public ResponseEntity<Integer> deleteItem(@RequestBody Map<String,Object> map) {
        System.out.println("class endPoint >> " + "/diet/delete/item");
        int result = dietService.itemDeleteRow(map);
        System.out.println("result >>" + result);
        if(result == 1){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }       

    // 식단 삭제
    @DeleteMapping("/delete/meal")
    public ResponseEntity<Integer> deleteRecord(@RequestParam Long diet_id) {
        System.out.println("class endPoint >> " + "/diet/delete/record");
        int result = dietService.deleteRecordRow(diet_id);
        System.out.println("result >>" + result);
        if(result == 1){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }

    // 특정 회원이 특정 날짜에 식사 타입별로 섭취한 영양성분 조회
    @GetMapping("/get/meal/nutrients")
    public ResponseEntity<List<Map<String,Object>>> mealNutCheck(@RequestBody Map<String,Object> map) {
        System.out.println("class endPoint >> " + "/diet/get/nutrients");
        List<Map<String,Object>> result = dietService.mealNutCheckRow(map);
        System.out.println("result >>" + result);
        if(result != null){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }
    
    // API로 받은 데이터를 기준으로 영양소 계산 엔드포인트
    @SuppressWarnings("null")
    @PostMapping("/calculate/nutrients")
    public ResponseEntity<ItemRequestDTO> calculateNutrients(@RequestBody FoodCalculateDTO foodCalcDTO) {
        try {
            ItemRequestDTO calculatedItem = foodCalcDTO.calculateNutrients();
            return new ResponseEntity<>(calculatedItem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    // 특정 회원의 TDEE 계산 엔드포인트
    @GetMapping("/calculate/tdee")
    public ResponseEntity<Map<String,Integer>> calculateTdee(@RequestParam Long memberId) {
        
        Map<String,String> info = dietService.getMemberInfoRow(memberId);
        // 기초대사량(BMR) 계산 - 해리스-베네딕트 공식 사용
        Integer bmr;
        if (info.get("profile_gender").equalsIgnoreCase("M")) {
            bmr = (int) Math.round(88.362 + (13.397 * Double.parseDouble(info.get("profile_weight")))
                        + (4.799 * Double.parseDouble(info.get("profile_height")))
                        - (5.677 * Double.parseDouble(info.get("profile_age"))));
        } else {
            bmr = (int) Math.round(447.593 + (9.247 * Double.parseDouble(info.get("profile_weight")))
                        + (3.098 * Double.parseDouble(info.get("profile_height")))
                        - (4.330 * Double.parseDouble(info.get("profile_age"))));
        }

        // 활동레벨에 따른 TDEE 계산
        double activityMultiplier;
        switch (info.get("profile_activity_level").toLowerCase()) {
            case "좌식생활":
                activityMultiplier = 1.2;     // 좌식생활
                break;
            case "가벼운 운동":
                activityMultiplier = 1.375;   // 가벼운 운동 (주1-3회)
                break;
            case "중간 강도 운동":
                activityMultiplier = 1.55;    // 중간 강도 운동 (주3-5회)
                break;
            case "활동적인 운동":
                activityMultiplier = 1.725;   // 활동적인 운동 (주6-7회)
                break;
            case "매우 활동적":
                activityMultiplier = 1.9;     // 매우 활동적 (하루 2회 운동)
                break;
            default:
                throw new IllegalArgumentException("잘못된 활동 레벨입니다");
        }

        Integer tdee = (int) Math.round(bmr * activityMultiplier);
        Integer recommendedCalories = tdee;
        boolean isMale = info.get("profile_gender").equalsIgnoreCase("M");

        // 다이어트 목표에 따른 칼로리 조정
    switch (info.get("profile_diet_goal").toLowerCase()) {
        case "다이어트":
            // 남성: TDEE - 300~500, 여성: TDEE - 200~300
            recommendedCalories = tdee - (isMale ? 400 : 250);
            break;
        case "벌크업":
            // 남성: TDEE + 300~500, 여성: TDEE + 200~400
            recommendedCalories = tdee + (isMale ? 400 : 300);
            break;
        case "린매스업":
            // 남성: TDEE + 200~400, 여성: TDEE + 100~200
            recommendedCalories = tdee + (isMale ? 300 : 150);
            break;
        case "유지":
            // TDEE 유지
            break;
        default:
            throw new IllegalArgumentException("잘못된 다이어트 목표입니다");
    }

    Map<String,Integer> result = new HashMap<>();
    result.put("bmr", bmr);
    result.put("tdee", tdee);
    result.put("recommendedCalories", recommendedCalories);

    return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
