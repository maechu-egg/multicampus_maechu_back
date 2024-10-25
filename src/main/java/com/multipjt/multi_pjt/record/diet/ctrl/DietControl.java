package com.multipjt.multi_pjt.record.diet.ctrl;

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
    
    // @PostMapping("path")
    // public String postMethodName(@RequestBody String entity) {
    //     //TODO: process POST request
        
    //     return entity;
    // }
    

}
