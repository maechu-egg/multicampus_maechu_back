package com.multipjt.multi_pjt.record.diet.ctrl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.badge.dao.UserActivityRecordMapper;
import com.multipjt.multi_pjt.jwt.JwtTokenProvider;
import com.multipjt.multi_pjt.record.diet.domain.DietRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.DietResponseDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemResponseDTO;
import com.multipjt.multi_pjt.record.diet.domain.MealType;
import com.multipjt.multi_pjt.record.diet.service.DietService;
import com.multipjt.multi_pjt.record.openApi.domain.FoodCalculateDTO;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("record/diet")
public class DietControl {
    
    @Autowired
    private DietService dietService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserActivityRecordMapper userActivityRecordMapper;

    //규칙 : 한 끼에 같은 식품 중복 작성 불가, 하루에 끼니 유형은 하나씩만 가능
    // 식단 수정 기능은 넣지 않음, 식단 기입 여부는 식품 기록 여부에 따라 결정

    // 사용자가 식품 입력 시 /record/api/nutrient 엔드포인트 호출해 관련 식품 최대 10개 조회
    // 이후 사용자가 특정 식품을 선택하면 양(g)을 입력받아 영양소 계산 엔드포인트 호출
    // 이후 계산된 영양소를 프론트에서 다시 한 번 보여준 후 , 추가 버튼을 통해 식단에 추가
    // 추가 시 식품 테이블에 추가
    // 만약 원하는 식품이 존재하지 않으면 "직접 입력" 버튼을 통해 직접 입력


    // 식단 추가
    // member_id, meal_type 필요
    @GetMapping("/insert/meal")
    public ResponseEntity<Object> mealInsert(@RequestParam(name = "meal_type") MealType meal_type,
                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        System.out.println("class endPoint >> " + "/record/diet/insert/meal");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            Integer member_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
                        
            Map<String,Object> map = new HashMap<>();
            map.put("member_id", member_id);
            map.put("meal_type", meal_type);
            
            int result = dietService.dietInsertRow(map);
            System.out.println("deubg >>> result : " + result);

            map.put("record_date", LocalDate.now());
            
            Integer dietNumber = dietService.findDietRow(map);
            System.out.println("debug >>> dietNumber : " + dietNumber);

            if(result == 1){

                Map<String,Object> point = new HashMap<>();
                System.out.println("debug >>> point insert start !!");
                
                point.put("activityType","diet");
                point.put("memberId",member_id);
                userActivityRecordMapper.insertActivityAndUpdatePoints(point);

                System.out.println("debug >>> point insert end !!");
                
                // 추가 성공
                return new ResponseEntity<>(dietNumber,HttpStatus.OK);
            } else{
                // 추가 실패
                return new ResponseEntity<>("추가 실패",HttpStatus.BAD_REQUEST);
            }
        } else{
            return new ResponseEntity<>("인증실패",HttpStatus.UNAUTHORIZED);
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
            // 추가 성공
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            // 추가 실패
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }
    }
    
    // 식단 번호 찾기
    // 일일 식단 조회를 통해 식단 번호를 갖기 때문에 프론트에서는 사용할 일이 없다
    @GetMapping("/get/dietnumber")
    public ResponseEntity<Object> findDietNumber(@RequestBody Map<String,Object> map,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        System.out.println("class endPoint >> " + "/record/diet/get/dietnumber");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try{
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                Integer member_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출

                System.out.println("debug >>> member_id : " + member_id);
                System.out.println("debug >>> token : " + token);

                map.put("member_id", member_id);

                Integer result = dietService.findDietRow(map);
                System.out.println("result >>" + result);

                if(result != null){
                    // 값이 있을 때
                    return new ResponseEntity<>(result,HttpStatus.OK);
                } else{
                    // 값이 없을 때
                    return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
                }
            } catch(Exception e){
                // 서버 내부 오류 발생
                System.out.println("서버 오류 발생: " + e.getMessage());
                return new ResponseEntity<>("서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            // 인증 실패
            return new ResponseEntity<>("인증실패",HttpStatus.UNAUTHORIZED); 
        }
    }

    // 식단 조회
    // 일일 식단 조회를 통해 식단 번호를 가지게 됨
    @PostMapping("/get/diet-and-items")
    public ResponseEntity<Object> findDietAndItems(
        @RequestBody Map<String, Object> map,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        
        System.out.println("class endPoint >> " + "/record/diet/get/diet-and-items");
    
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                Integer member_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
                Integer diet_id = null;
                System.out.println("debug >>> member_id : " + member_id);
                System.out.println("debug >>> token : " + token);
    
                map.put("member_id", member_id);
    
                // 1. 식단 조회
                DietResponseDTO dietResult = dietService.dietFindRow(map);
                System.out.println("dietResult >> " + dietResult);
    
                if (dietResult != null) {
                    // 2. 식품 조회
                    diet_id = dietResult.getDiet_id(); // diet_id 가져오기
                    Map<String,Object> result = new HashMap<>();
                    result.put("diet_id", diet_id);
                    List<ItemResponseDTO> itemsResult = dietService.itemFindAllRow(diet_id);
                    result.put("itemList", itemsResult);
                    System.out.println("itemsResult >> " + itemsResult);
        
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    // 식단 조회 결과가 없을 때
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            } catch (Exception e) {
                // 서버 내부 오류 발생
                System.out.println("서버 오류 발생: " + e.getMessage());
                return new ResponseEntity<>("서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            // 인증 실패
            return new ResponseEntity<>("인증 실패", HttpStatus.UNAUTHORIZED);
        }
    }

    // 식단 수정
    // 일일 식단 조회를 통해 식단 번호를 알고 있는 상태
    // 식사타입만 변경 가능
    // 식단 조회를 통해 식단 타입들을 알고 있는 상태
    @PutMapping("/update/meal")
    public ResponseEntity<Object> mealUpdate(@RequestBody Map<String, Object> map) {
        System.out.println("class endPoint >> " + "/record/diet/update/meal");
    
        List<DietResponseDTO> list = dietService.dietFindAllRow(map);
    
        for (DietResponseDTO i : list) {
            if (i.getMeal_type().equalsIgnoreCase(map.get("meal_type").toString())) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 동일한 meal_type이 있는 경우 중단
            }
        }
    
        int result = dietService.mealUpdateRow(map);
        System.out.println("result >> " + result);
    
        if (result == 1) {
            return new ResponseEntity<>(result, HttpStatus.OK); // 수정 성공
        } else {
            return new ResponseEntity<>("수정 실패", HttpStatus.BAD_REQUEST); // 수정 실패
        }
    }

    // 식품 수정, 사용자는 양만 변경 가능, 그러면 영양성분 또한 그대로 반영, 식품은 변경 불가능
    // 이거는 그냥 구해져있는 영양성분에 수정된 칼로리 / 현재 칼로리 한 비율 다 곱해주면 되겠네
    // 프론트에서 처리하고 값 넣으면 되겠네
    @PutMapping("/update/item")
    public ResponseEntity<Object> itemUpdate(@RequestBody ItemRequestDTO itemRequestDTO) {
    
        System.out.println("class endPoint >> " + "/record/diet/update/item");
    
        int result = dietService.itemUpdateRow(itemRequestDTO);
        System.out.println("result >>" + result);
        if(result == 1){
            // 수정 성공
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            // 수정 실패
            return new ResponseEntity<>("수정 실패",HttpStatus.BAD_REQUEST);
        }
    }

    // 식품 삭제
    // 식품 조회를 통해 식품 번호를 알고 있는 상태
    @DeleteMapping("/delete/item")
    public ResponseEntity<Object> deleteItem(@RequestParam(name = "item_id") Integer item_id) {
        System.out.println("class endPoint >> " + "/record/diet/delete/item");
        int result = dietService.itemDeleteRow(item_id);
        System.out.println("result >>" + result);
        if(result == 1){
            // 삭제 성공
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            // 삭제 실패
            return new ResponseEntity<>("삭제 실패",HttpStatus.BAD_REQUEST);
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
            // 삭제 성공
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else{
            // 삭제 실패
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    // 특정 회원이 특정 날짜에 식사 타입별로 섭취한 영양성분 조회
    // record_date, member_id 필요
    @GetMapping("/get/meal/nutrients")
        public ResponseEntity<Object> mealNutCheck(@RequestParam(name = "record_date") String record_date,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        System.out.println("class endPoint >> " + "/record/diet/get/meal/nutrients");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            Integer member_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출

            System.out.println("debug >>> member_id : " + member_id);
            System.out.println("debug >>> token : " + token);
            try{

                Map<String,Object> map = new HashMap<>();
                map.put("member_id",member_id);
                System.out.println("debug >>> map : " + map);

                List<Map<String,Object>> result = dietService.mealNutCheckRow(map);
                System.out.println("result >>" + result);
                if(!result.isEmpty()){
                    // 값이 있을 때
                    return new ResponseEntity<>(result,HttpStatus.OK);
                } else{
                    // 값이 없을 때
                    return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
                }
            } catch(Exception e){
                // 3. 서버 내부 오류 발생
                System.out.println("서버 오류 발생: " + e.getMessage());
                return new ResponseEntity<>("서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("인증실패",HttpStatus.UNAUTHORIZED);
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
            // 계산 성공
            return new ResponseEntity<>(calculatedItem, HttpStatus.OK);
        } catch (Exception e) {
            // 계산 실패
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // 특정 회원의 TDEE 계산 엔드포인트
    // 회원 정보 조회를 통해 계산
    @GetMapping("/calculate/tdee")
    public ResponseEntity<Object> calculateTdee(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        System.out.println("class endPoint >> " + "/record/diet/calculate/tdee");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            Integer member_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출

            System.out.println("debug >>> member_id : " + member_id);
            System.out.println("debug >>> token : " + token);

            Map<String,Object> result = dietService.calculateTdeeRow(member_id);
            // TDEE 계산 성공
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else{
            return new ResponseEntity<>("인증실패",HttpStatus.UNAUTHORIZED);
        }
    }
    // 특정 달 식단 기록 날짜 조회  
    @PostMapping("/get/month")
    public ResponseEntity<Object> getMonthDiet(@RequestBody Map<String,Object> map,
                                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        System.out.println("class endPoint >> " + "/record/diet/get/month");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try{
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                Integer member_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출

                System.out.println("debug >>> member_id : " + member_id);
                System.out.println("debug >>> token : " + token);

                map.put("member_id", member_id);
                System.out.println("debug >>> map : " + map);

                List<Map<String,Object>> result = dietService.getMonthDietRow(map);

                System.out.println("result >>" + result);

                if(!result.isEmpty()){
                    // 기록이 있을 때
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else{
                    // 기록이 없을 때
                    return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
                }
            } catch(Exception e){
                // 3. 서버 내부 오류 발생
                System.out.println("서버 오류 발생: " + e.getMessage());
                return new ResponseEntity<>("서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);    
            } 
        } else {
            // 인증 실패
            return new ResponseEntity<>("인증실패",HttpStatus.UNAUTHORIZED);
        }
    }
}
