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
import com.multipjt.multi_pjt.record.diet.domain.DietResponseDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemResponseDTO;
import com.multipjt.multi_pjt.record.diet.service.DietService;
import com.multipjt.multi_pjt.record.openApi.domain.FoodCalculateDTO;

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

    // 사용자가 식품 입력 시 /record/api/nutrient 엔드포인트 호출해 관련 식품 최대 10개 조회
    // 이후 사용자가 특정 식품을 선택하면 양(g)을 입력받아 영양소 계산 엔드포인트 호출
    // 이후 계산된 영양소를 프론트에서 다시 한 번 보여준 후 , 추가 버튼을 통해 식단에 추가
    // 추가 시 식품 테이블에 추가
    // 만약 원하는 식품이 존재하지 않으면 "직접 입력" 버튼을 통해 직접 입력


    // 식단 추가
    // record_date, member_id, meal_type 필요
    @PostMapping("/insert/meal")
    public ResponseEntity<Integer> mealInsert(@RequestBody DietRequestDTO dietRequestDTO) {
        System.out.println("class endPoint >> " + "/record/diet/insert/meal");
        int result = dietService.dietInsertRow(dietRequestDTO);
        System.out.println("result >>" + result);
        if(result == 1){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }
    // 식품 추가
    // API로 받은 식품 데이터가 없거나, 원하는 게 없을 때도 사용
    // 일일 식단 조회를 통해 식단 번호를 알고 있는 상태
    // 이벤트를 통해 식사타입을 구별할 거 기 때문에 식사타입 또한 알고 있음
    // 날짜는 LocalDate 타입으로 받아오기 때문에 따로 처리 필요 없음, 해당 일 아니면 추가 불가능하게
    @PostMapping("/insert/item")
    public ResponseEntity<Integer> itemInsert(@RequestBody ItemRequestDTO itemRequestDTO) {
        System.out.println("class endPoint >> " + "/record/diet/insert/item");
        int result = dietService.itemInsertRow(itemRequestDTO);
        System.out.println("result >>" + result);
        if(result == 1){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }
    
    // 식단 번호 찾기
    // 일일 식단 조회를 통해 식단 번호를 갖기 때문에 프론트에서는 사용할 일이 없다
    @GetMapping("/get/dietnumber")
    public ResponseEntity<Integer> findDietNumber(@RequestBody Map<String,Object> map) {
        System.out.println("class endPoint >> " + "/record/diet/get/dietnumber");
        Integer result = dietService.findDietRow(map);
        System.out.println("result >>" + result);
        if(result != null){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }

    // 식단 조회
    // 일일 식단 조회를 통해 식단 번호를 가지게 됨
    @GetMapping("/get/diet")
    public ResponseEntity<List<DietResponseDTO>> findDiet(@RequestBody Map<String,Object> map) {
        System.out.println("class endPoint >> " + "/record/diet/get/diet");
        List<DietResponseDTO> result = dietService.dietFindAllRow(map);
        System.out.println("result >>" + result);
        if(result != null){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }

    // 식품 조회
    // 일일 식단 조회를 통해 식단 번호를 알고 있는 상태
    // 이 엔드포인트를 통해 식품 번호를 알 수 있게 됨
    @GetMapping("/get/items")
    public ResponseEntity<List<ItemResponseDTO>> findDietItems(@RequestParam(name = "diet_id") Integer diet_id) {
        System.out.println("class endPoint >> " + "/record/diet/get/items");
        List<ItemResponseDTO> result = dietService.itemFindAllRow(diet_id);
        System.out.println("result >>" + result);
        if(result != null){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }

    // 식단 수정
    // 일일 식단 조회를 통해 식단 번호를 알고 있는 상태
    // 식사타입만 변경 가능
    // 식단 조회를 통해 식단 타입들을 알고 있는 상태
    @PutMapping("/update/meal")
    public ResponseEntity<Integer> mealUpdate(@RequestBody DietRequestDTO dietRequestDTO) {
        System.out.println("class endPoint >> " + "/record/diet/update/meal");
        int result = dietService.mealUpdateRow(dietRequestDTO);
        System.out.println("result >>" + result);
        if(result == 1){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }



    // 식품 수정, 사용자는 양만 변경 가능, 그러면 영양성분 또한 그대로 반영, 식품은 변경 불가능
    // 이거는 그냥 구해져있는 영양성분에 수정된 칼로리 / 현재 칼로리 한 비율 다 곱해주면 되겠네
    // 프론트에서 처리하고 값 넣으면 되겠네
    @PutMapping("/update/item")
    public ResponseEntity<Integer> itemUpdate(@RequestBody ItemRequestDTO itemRequestDTO) {
    
        System.out.println("class endPoint >> " + "/record/diet/update/item");
    
        int result = dietService.itemUpdateRow(itemRequestDTO);
        System.out.println("result >>" + result);
        if(result == 1){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }

    // 식품 삭제
    // 식품 조회를 통해 식품 번호를 알고 있는 상태
    @DeleteMapping("/delete/item")
    public ResponseEntity<Integer> deleteItem(@RequestParam(name = "item_id") Integer item_id) {
        System.out.println("class endPoint >> " + "/record/diet/delete/item");
        int result = dietService.itemDeleteRow(item_id);
        System.out.println("result >>" + result);
        if(result == 1){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }       

    // 식단 삭제
    // 일일 식단 조회를 통해 식단 번호를 알고 있는 상태
    @DeleteMapping("/delete/meal")
    public ResponseEntity<Integer> deleteRecord(@RequestParam(name = "diet_id") Integer diet_id) {
        System.out.println("class endPoint >> " + "/record/diet/delete/meal");
        int result = dietService.deleteRecordRow(diet_id);
        System.out.println("result >>" + result);
        if(result == 1){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }

    // 특정 회원이 특정 날짜에 식사 타입별로 섭취한 영양성분 조회
    // record_date, member_id 필요
    @GetMapping("/get/meal/nutrients")
        public ResponseEntity<List<Map<String,Object>>> mealNutCheck(@RequestBody Map<String,Object> map) {
        System.out.println("class endPoint >> " + "/record/diet/get/meal/nutrients");
        List<Map<String,Object>> result = dietService.mealNutCheckRow(map);
        System.out.println("result >>" + result);
        if(result != null){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }
    
    // API로 받은 데이터를 기준으로 영양소 계산 엔드포인트
    // 사용자가 입력한 양(g)과 api에서 제공하는 양(g)의 비율을 곱해 영양소 계산
    @SuppressWarnings("null")
    @PostMapping("/calculate/nutrients")
    public ResponseEntity<ItemRequestDTO> calculateNutrients(@RequestBody FoodCalculateDTO foodCalcDTO) {
        System.out.println("class endPoint >> " + "/record/diet/calculate/nutrients");
        try {
            ItemRequestDTO calculatedItem = foodCalcDTO.calculateNutrients();
            return new ResponseEntity<>(calculatedItem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // 특정 회원의 TDEE 계산 엔드포인트
    // 회원 정보 조회를 통해 계산
    @GetMapping("/calculate/tdee")
    public ResponseEntity<Map<String,Object>> calculateTdee(@RequestParam(name = "member_id") Integer memberId) {
        System.out.println("class endPoint >> " + "/record/diet/calculate/tdee");
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
        switch (Integer.parseInt(info.get("profile_activity_level"))) {
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
        
        Integer proteinRate;
        Integer fatRate;
        Integer carbRate;        

        // 다이어트 목표에 따른 칼로리 조정
    switch (info.get("profile_diet_goal").toLowerCase()) {
        case "다이어트":
            recommendedCalories = (int) Math.round(tdee * 0.8);

            carbRate = 3;
            proteinRate = 5;
            fatRate = 2;

            recommendedCarb = (recommendedCalories * carbRate) / 4;
            recommendedProtein = (recommendedCalories * proteinRate) / 4;
            recommendedFat = (recommendedCalories * fatRate) / 9;
            break;
        case "벌크업":
            recommendedCalories = (int) Math.round(tdee * 1.2);
            carbRate = 6;
            proteinRate = 3;
            fatRate = 1;

            recommendedCarb = (recommendedCalories * carbRate) / 4;
            recommendedProtein = (recommendedCalories * proteinRate) / 4;
            recommendedFat = (recommendedCalories * fatRate) / 9;
            break;
        case "린매스업":
            recommendedCalories = (int) Math.round(tdee * 1.1);
            carbRate = 4;
            proteinRate = 4;
            fatRate = 2;

            recommendedCarb = (recommendedCalories * carbRate) / 4;
            recommendedProtein = (recommendedCalories * proteinRate) / 4;
            recommendedFat = (recommendedCalories * fatRate) / 9;
            break;
        case "유지":
            recommendedCalories = tdee;
            carbRate = 5;
            proteinRate = 3;
            fatRate = 2;

            recommendedCarb = (recommendedCalories * carbRate) / 4;
            recommendedProtein = (recommendedCalories * proteinRate) / 4;
            recommendedFat = (recommendedCalories * fatRate) / 9;
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
        result.put("weight", Double.parseDouble(info.get("profile_weight")));
        result.put("dietGoal", info.get("profile_diet_goal"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 특정 달 식단 기록 날짜 조회
    @PostMapping("/get/month")
    public ResponseEntity<List<String>> getMonthDiet(@RequestBody Map<String,Object> map) {
        System.out.println("class endPoint >> " + "/record/diet/get/month");
        List<String> result = dietService.getMonthDietRow(map);

        System.out.println("result >>" + result);

        if(result != null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }
}
