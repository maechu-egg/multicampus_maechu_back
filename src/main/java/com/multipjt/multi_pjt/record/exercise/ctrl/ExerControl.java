package com.multipjt.multi_pjt.record.exercise.ctrl;

import java.util.List;
import java.util.Map;

import java.lang.Long;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.record.exercise.domain.ExerRequestDTO;
import com.multipjt.multi_pjt.record.exercise.domain.ExerResponseDTO;
import com.multipjt.multi_pjt.record.exercise.domain.SetRequestDTO;
import com.multipjt.multi_pjt.record.exercise.domain.SetResponseDTO;
import com.multipjt.multi_pjt.record.exercise.service.ExerService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("record/exercise")
public class ExerControl {
    
    @Autowired
    private ExerService exerService;
    
    //규칙 : 세트는 칼로리 계산에 포함되지 않음, 하루에 같은 운동 여러번 가능(운동 번호를 통해 순서 구분), 세트 기입은 선택 사항


    // 운동 추가
    @PostMapping("/insert/type")
    public ResponseEntity<Integer> exerInsert(@RequestBody ExerRequestDTO exerRequestDTO){
        System.out.println("class endPoint >> " + "/record/exercise/insert/type");
        System.out.println("exerRequestDTO >> " + exerRequestDTO);
        int result = exerService.exerInsertRow(exerRequestDTO);
        System.out.println("result >> " + result);
        if(result == 1){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 번호 찾기
    @GetMapping("/get/exerciseId")
    public ResponseEntity<List<Long>> exerIdGet(@RequestBody Map<String,Object> map){
        System.out.println("class endPoint >> " + "/record/exercise/get/exerciseId");
        System.out.println("map >> " + map);
        List<Long> exerciseId = exerService.exerIdGetRow(map);
        if(exerciseId != null && !exerciseId.isEmpty()){
            exerciseId.forEach(num -> System.out.println("exerciseId >> " + num));
            return new ResponseEntity<>(exerciseId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(exerciseId, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 세트 추가
    @PostMapping("/insert/set")
    public ResponseEntity<Integer> setInsert(@RequestBody SetRequestDTO setRequestDTO){
        System.out.println("class endPoint >> " + "/record/exercise/insert/set");
        System.out.println("setRequestDTO >> " + setRequestDTO);
        int result = exerService.setInsertRow(setRequestDTO);
        System.out.println("result >> " + result);
        if(result == 1){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 세트 정보 출력
    @GetMapping("/get/setInfo")
    public ResponseEntity<SetResponseDTO> setInfoGet(@RequestParam(name = "exercise_id") Long exercise_id){
        System.out.println("class endPoint >> " + "/record/exercise/get/setInfo");  
        System.out.println("exercise_id >> " + exercise_id);
        SetResponseDTO setResponseDTO = exerService.setInfoGetRow(exercise_id);
        System.out.println("setResponseDTO >> " + setResponseDTO);
        if(setResponseDTO != null){
            return new ResponseEntity<>(setResponseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(setResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 일자별 운동 목록 찾기
    @GetMapping("/get/exerday")
    public ResponseEntity<List<ExerResponseDTO>> exerDayGet(@RequestBody Map<String,Object> map){
        System.out.println("class endPoint >> " + "/record/exercise/get/exerday");
        System.out.println("map >> " + map);
        List<ExerResponseDTO> exerResponseDTO = exerService.exerDayGetRow(map);
        System.out.println("exerResponseDTO >> " + exerResponseDTO);
        if(exerResponseDTO != null){
            return new ResponseEntity<>(exerResponseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(exerResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 종류 수정, 시간, 횟수만 수정 가능, 둘 다 또는 하나만 수정 시 칼로리 자동 수정
    @PutMapping("/update/exer")
    public ResponseEntity<Integer> exerUpdate(@RequestBody Map<String,Object> map){
        System.out.println("class endPoint >> " + "/record/exercise/update/exer");
        System.out.println("map >> " + map);
        int result = exerService.exerUpdateRow(map);
        System.out.println("result >> " + result);
        if(result == 1){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 세트 수정
    @PutMapping("/update/set")
    public ResponseEntity<Integer> setUpdate(@RequestBody Map<String,Object> map){
        System.out.println("class endPoint >> " + "/record/exercise/update/set");
        System.out.println("map >> " + map);
        int result = exerService.setUpdateRow(map);
        System.out.println("result >> " + result);
        if(result == 1){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 삭제
    @DeleteMapping("/delete/exer")
    public ResponseEntity<Integer> exerDelete(@RequestParam(name = "exercise_id") Long exercise_id){
        System.out.println("class endPoint >> " + "/record/exercise/delete/exer");
        System.out.println("exercise_id >> " + exercise_id);
        int result = exerService.exerDeleteRow(exercise_id);
        System.out.println("result >> " + result);
        if(result == 1){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 세트 삭제
    @DeleteMapping("/delete/set")
    public ResponseEntity<Integer> setDelete(@RequestParam(name = "set_id") Long set_id){
        System.out.println("class endPoint >> " + "/record/exercise/delete/set");
        System.out.println("set_id >> " + set_id);    
        int result = exerService.setDeleteRow(set_id);
        System.out.println("result >> " + result);
        if(result == 1){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    // 일일 운동 별 칼로리 총합 출력 
    @GetMapping("/get/exerCalories")
    public ResponseEntity<List<Map<String,Object>>> exerCaloriesGet(@RequestBody Map<String,Object> map){
        System.out.println("class endPoint >> " + "/record/exercise/get/exerCalories");
        System.out.println("map >> " + map);
        List<Map<String,Object>> list = exerService.exerCaloriesGetRow(map);
        System.out.println("exer list >> " + list);
        if(list != null && !list.isEmpty()){
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
        }
    }   
}
