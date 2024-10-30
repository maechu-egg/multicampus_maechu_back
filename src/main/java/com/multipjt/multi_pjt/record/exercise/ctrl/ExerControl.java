package com.multipjt.multi_pjt.record.exercise.ctrl;

import java.util.HashMap;
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
    public ResponseEntity<Long> exerInsert(@RequestBody ExerRequestDTO exerRequestDTO){
        System.out.println("class endPoint >> " + "/record/exercise/insert/type");
        // 강도, 시간, 운동 종류, member_id 만 입력받고, 칼로리는 계산해서 넣어야 함
        System.out.println("exerRequestDTO >> " + exerRequestDTO);

        // met 값 계산
        List<Map<String,Object>> info = exerService.metGetRow(exerRequestDTO.getExercise_type());
        System.out.println("ExerciseMet info >> " + info);

        Float met = null;
        // 사용자가 입력한 강도와 csv 파일에 있는 강도가 같으면 met 값 가져오기
        for(Map<String,Object> map : info){
            String intensity = map.get("intensity").toString();
            if(intensity != null && intensity.trim().equalsIgnoreCase(exerRequestDTO.getIntensity().toString().trim())){
                System.out.println("강도가 존재하는 운동입니다");
                met = (Float) map.get("exercise_met");
                break;
            }   
        }

        // 사용자가 입력한 운동이 csv에 존재하지 않는 운동이면 기본 met 값 설정
        if(met == null){
            switch(exerRequestDTO.getIntensity().toString()){
                case "LOW":
                    met = 3.0F;
                    break;
                case "MEDIUM":
                    met = 5.0F;
                    break;
                case "HIGH":
                    met = 7.0F;
                    break;
            }
        }
        System.out.println("met >> " + met);

        // 몸무게 가져오기
        Float weight = exerService.getMemberInfoRow(exerRequestDTO.getMember_id());
        System.out.println("weight >> " + weight);

        // 칼로리 계산, met * 3.5 * 몸무게 * 운동 시간 * 5
        Float calories = met * 3.5F * weight * exerRequestDTO.getDuration() * 5F;
        System.out.println("calories >> " + calories);
        exerRequestDTO.setCalories(calories);

        Long exercise_id = exerService.exerInsertRow(exerRequestDTO);
        System.out.println("exercise_id >> " + exercise_id);

        // 운동 번호 반환
        if(exercise_id != null){
            return new ResponseEntity<>(exercise_id, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(exercise_id, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 번호 찾기
    // 프론트 구현하면 사용 안 할 거임
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
    // 운동 추가를 통해 exercise_id를 프론트가 가지고 있는 상태
    // 값을 넣지 않는 항목은 0을 부여
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

    // 운동 세트 정보 조회
    // 일일 운동 조회를 통해 exercise_id를 프론트가 가지고 있는 상태
    // set_id 값을 기준으로 나열, 세트 정보 조회를 통해 set_id 값을 프론트가 가짐
    // 값이 0인 항목은 출력 x 
    @GetMapping("/get/setInfo")
    public ResponseEntity<List<SetResponseDTO>> setInfoGet(@RequestParam(name = "exercise_id") Long exercise_id){
        System.out.println("class endPoint >> " + "/record/exercise/get/setInfo");  
        System.out.println("exercise_id >> " + exercise_id);
        List<SetResponseDTO> setResponseDTOs = exerService.setInfoGetRow(exercise_id);
        System.out.println("setResponseDTOs >> " + setResponseDTOs);
        if(setResponseDTOs != null && !setResponseDTOs.isEmpty()){
            return new ResponseEntity<>(setResponseDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(setResponseDTOs, HttpStatus.BAD_REQUEST);
        }
    }

    // 일일 운동 조회
    // 일일 운동 조회 시 운동 정보를 프론트가 가지고 있는 상태
    @GetMapping("/get/exerday")
    public ResponseEntity<List<ExerResponseDTO>> exerDayGet(@RequestBody Map<String,Object> map){
        System.out.println("class endPoint >> " + "/record/exercise/get/exerday");
        System.out.println("map >> " + map);
        List<ExerResponseDTO> exerResponseDTOs = exerService.exerDayGetRow(map);
        System.out.println("exerResponseDTOs >> " + exerResponseDTOs);
        if(exerResponseDTOs != null && !exerResponseDTOs.isEmpty()){
            return new ResponseEntity<>(exerResponseDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(exerResponseDTOs, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 종류 수정 -> 시간, 강도만 수정 가능, 둘 다 또는 하나만 수정 시 칼로리 자동 수정
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
    // 세트 정보 조회를 통해 set_id를 프론트가 가지고 있는 상태
    // 출력하고 싶지 않은 항목은 값 0 부여
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
    // 일일 운동 조회를 통해 exercise_id를 프론트가 가지고 있는 상태
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
    // 세트 정보 조회를 통해 set_id를 프론트가 가지고 있는 상태
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

    // 특정회원의 일일 운동 별 칼로리 총합 출력 
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
