package com.multipjt.multi_pjt.record.exercise.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import com.multipjt.multi_pjt.jwt.JwtTokenProvider;

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
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("record/exercise")
public class ExerControl {
    
    @Autowired
    private ExerService exerService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //규칙 : 세트는 칼로리 계산에 포함되지 않음, 하루에 같은 운동 여러번 가능(운동 번호를 통해 순서 구분), 세트 기입은 선택 사항


    // 운동 추가
    @PostMapping("/insert/type")
    public ResponseEntity<Object> exerInsert(@RequestBody ExerRequestDTO exerRequestDTO,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        System.out.println("class endPoint >> " + "/record/exercise/insert/type");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            exerRequestDTO.setMember_id(jwtTokenProvider.getUserIdFromToken(token));

            // 강도, 시간, 운동 종류, member_id 만 입력받고, 칼로리는 계산해서 넣어야 함
            System.out.println("exerRequestDTO >> " + exerRequestDTO);

            // met 값 계산
            List<Map<String,Object>> info = exerService.metGetRow(exerRequestDTO.getExercise_type());
            System.out.println("ExerciseMet info >> " + info);

            Double met = null;
            // 사용자가 입력한 운동이 csv에 존재하는 운동이면 met 값 가져오기
            // 사용자가 입력한 강도와 csv 파일에 있는 강도가 같으면 met 값 가져오기
            if(!info.isEmpty()){
                System.out.println("csv에 존재하는 운동입니다");
                Double tempMet = null;
                for(Map<String,Object> map : info){
                    String intensity = (String) map.get("intensity");
                    if(intensity != null && intensity.equalsIgnoreCase(exerRequestDTO.getIntensity().toString())){
                        System.out.println("강도도 존재하는 운동입니다");
                        met = (Double) map.get("exercise_met");
                        break;
                    }
                    else if(intensity == null){
                        tempMet = (Double) map.get("exercise_met");
                    }
            }
            if(met == null){
                System.out.println("강도는 존재하지 않는 운동입니다");
                if (tempMet != null) {
                    switch(exerRequestDTO.getIntensity().toString()){
                        case "LOW":
                            met = tempMet / 1.5;
                            break;
                        case "GENERAL":
                            met = tempMet;
                            break;
                        case "HIGH":
                            met = tempMet * 1.5;
                            break;
                    }
                } else {
                    // tempMet이 null인 경우 기본값 설정
                    switch(exerRequestDTO.getIntensity().toString()){
                        case "LOW":
                            met = 3.0;
                            break;
                        case "GENERAL":
                            met = 5.0;
                            break;
                        case "HIGH":
                            met = 7.0;
                            break;
                    }
                }       
            }
            }
            else{  // 사용자가 입력한 운동이 csv에 존재하지 않는 운동이면 기본 met 값 설정
                System.out.println("csv에 존재하지 않는 운동입니다");
                switch(exerRequestDTO.getIntensity().toString()){
                    case "LOW":
                        met = 3.0;
                        break;
                    case "GENERAL":
                        met = 5.0;
                        break;
                    case "HIGH":
                        met = 7.0;
                        break;
                }
            }
            System.out.println("met >> " + met);
            exerRequestDTO.setMet(met);
            
            // 몸무게 가져오기
            Double weight = exerService.getMemberInfoRow(exerRequestDTO.getMember_id());
            System.out.println("weight >> " + weight);

            // 칼로리 계산, met * 3.5 * 몸무게 * 운동 시간 * 5
            if (met != null) {
                Integer calories = (int) ((met * 3.5 * weight * exerRequestDTO.getDuration()) / 1000 * 5);
                System.out.println("calories >> " + calories);
                exerRequestDTO.setCalories(calories);
            }

            Integer exercise_id = exerService.exerInsertRow(exerRequestDTO);
            System.out.println("exercise_id >> " + exercise_id);
            System.out.println("Result exerRequestDTO >> " + exerRequestDTO);
        
            // 운동 번호 반환
            // 운동 추가 성공
            return new ResponseEntity<>(exercise_id, HttpStatus.OK);
        } else {
            // 인증 실패
            return new ResponseEntity<>("인증 실패",HttpStatus.UNAUTHORIZED);
        }
    }

    // 운동 찾기
    // 일일 운동 조회를 통해 exercise_id를 프론트가 가지고 있는 상태
    @GetMapping("/get/exercise")
    public ResponseEntity<Object> exerGet(@RequestParam(name = "exercise_id") Integer exercise_id){
        // exercise_id만 입력
        System.out.println("class endPoint >> " + "/record/exercise/get/exerciseId");
        System.out.println("exercise_id >> " + exercise_id);
        try{
        ExerResponseDTO exerResponseDTO = exerService.exerGetRow(exercise_id);
            if(exerResponseDTO != null){
            
                System.out.println("exerResponseDTO >> " + exerResponseDTO);
                // 조회 성공
                return new ResponseEntity<>(exerResponseDTO, HttpStatus.OK);
            } else {
                // 조회 실패
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch(Exception e){
            // 서버 내부 오류 발생
            System.out.println("서버 오류 발생: " + e.getMessage());
            return new ResponseEntity<>("서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);    
        }
    }

    // 운동 세트 추가
    // 운동 추가를 통해 exercise_id를 프론트가 가지고 있는 상태
    // 값을 넣지 않는 항목은 0을 부여
    // set_id, distance, weight, repetitions, exercise_id -> setRequestDTO 모든 값 받음
    @PostMapping("/insert/set")
    public ResponseEntity<Integer> setInsert(@RequestBody List<SetRequestDTO> setRequestDTOs) {
        System.out.println("class endPoint >> " + "/record/exercise/insert/set");
        System.out.println("setRequestDTOs >> " + setRequestDTOs);
        
        int successCount = 0;
        for (SetRequestDTO dto : setRequestDTOs) {
            try {
                successCount += exerService.setInsertRow(dto);
            } catch (Exception e) {
                // 세트 추가 실패
                System.out.println("세트 추가 실패: " + e.getMessage());
            }
        }
        if (successCount == setRequestDTOs.size()) {
            System.out.println("세트 추가 성공");
            return new ResponseEntity<>(successCount, HttpStatus.OK);
        } else if (successCount > 0) {
            // 일부만 성공한 경우
            System.out.println("일부만 성공한 경우");
            return new ResponseEntity<>(successCount, HttpStatus.PARTIAL_CONTENT);
        } else {
            System.out.println("세트 추가 실패");
            return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 세트 정보 조회
    // 일일 운동 조회를 통해 exercise_id를 프론트가 가지고 있는 상태
    // set_id 값을 기준으로 나열, 세트 정보 조회를 통해 set_id 값을 프론트가 가짐
    // 값이 0인 항목은 출력 x 
    @GetMapping("/get/setInfo")
    public ResponseEntity<Object> setInfoGet(@RequestParam(name = "exercise_id") Integer exercise_id){
        System.out.println("class endPoint >> " + "/record/exercise/get/setInfo");  
        System.out.println("exercise_id >> " + exercise_id);
        try{
            List<SetResponseDTO> setResponseDTOs = exerService.setInfoGetRow(exercise_id);
            System.out.println("setResponseDTOs >> " + setResponseDTOs);
            if(!setResponseDTOs.isEmpty()){
                // 값이 있을 때
                return new ResponseEntity<>(setResponseDTOs, HttpStatus.OK);
            } else {
                // 값이 없을 때
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch(Exception e){
            // 서버 내부 오류 발생
            System.out.println("서버 오류 발생: " + e.getMessage());
            return new ResponseEntity<>("서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 일일 운동 조회
    // 일일 운동 조회 시 운동 정보를 프론트가 가지고 있는 상태가 됨
    @GetMapping("/get/exerday")
    public ResponseEntity<Object> exerDayGet(@RequestParam(name = "record_date") String record_date,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        System.out.println("class endPoint >> " + "/record/exercise/get/exerday");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            Integer member_id = jwtTokenProvider.getUserIdFromToken(token);
            
            System.out.println("debug >>> token : " + token);
            System.out.println("debug >>> member_id : " + member_id);

            Map<String,Object> map = new HashMap<>();
            map.put("member_id",member_id);
            map.put("record_date",record_date);

            System.out.println("debug >>> map : " + map);

            try {
                List<ExerResponseDTO> exerResponseDTOs = exerService.exerDayGetRow(map);
                System.out.println("exerResponseDTOs >> " + exerResponseDTOs);
            
                if(!exerResponseDTOs.isEmpty()){
                    // 1. 데이터를 정상적으로 찾은 경우
                    return new ResponseEntity<>(exerResponseDTOs, HttpStatus.OK);
                } else {
                    // 2. 데이터가 없는 경우
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            } catch (Exception e) {
                // 3. 서버 내부 오류 발생
                System.out.println("서버 오류 발생: " + e.getMessage());
                return new ResponseEntity<>("서버오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {   
            return new ResponseEntity<>("인증 실패",HttpStatus.UNAUTHORIZED);
        }
    }

    // 운동 종류 수정 -> 시간, 강도만 수정 가능, 둘 다 또는 하나만 수정 시 칼로리 자동 수정
    // exercise_id, duration, intensity 값만 넣어주면 met, calories 자동 수정
    @PutMapping("/update/exer")
    public ResponseEntity<Integer> exerUpdate(@RequestBody Map<String,Object> updateExerInfo) {
        System.out.println("class endPoint >> " + "/record/exercise/update/exer");
        System.out.println("변경 값 >> " + updateExerInfo);

        Integer exerciseId = Integer.valueOf(String.valueOf(updateExerInfo.get("exercise_id")));
        System.out.println("exerciseId >> " + exerciseId);

        ExerResponseDTO exerResponseDTO = exerService.exerGetRow(exerciseId);
        if (exerResponseDTO == null) {
            System.out.println("debug >>> exerResponseDTO is null");
            return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
        }
        // 강도와 MET 값 계산
        Double met = calculateMet(exerResponseDTO, updateExerInfo);
        System.out.println("met >> " + met);
        // 운동 시간 계산
        Integer duration = calculateDuration(exerResponseDTO, updateExerInfo);
        System.out.println("duration >> " + duration);
        
        // 칼로리 계산
        Double weight = exerService.getMemberInfoRow(exerResponseDTO.getMember_id());
        System.out.println("weight >> " + weight);
        
        Integer calories = calculateCalories(met, weight, duration);
        System.out.println("calories >> " + calories);
        
        // updateExerInfo에 계산된 값들 추가
        updateExerInfo.put("met", met);
        updateExerInfo.put("calories", calories);
        System.out.println("수정 값 반영된 calories, met >> " + updateExerInfo);            
        int result = exerService.exerUpdateRow(updateExerInfo);
        // 성공 -> 200, 실패 -> 400
        return new ResponseEntity<>(result, result == 1 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    private Double calculateMet(ExerResponseDTO exerResponseDTO, Map<String,Object> updateExerInfo) {
        String newIntensity = updateExerInfo.get("intensity").toString();
        
        // 강도가 변경되지 않은 경우 return
        if (exerResponseDTO.getIntensity().equalsIgnoreCase(newIntensity)) {
            return exerResponseDTO.getMet();
        }
        
        // CSV에서 MET 값 조회
        // CSV에 변경된 강도와 같은 운동이 존재 시 return
        List<Map<String,Object>> info = exerService.metGetRow(exerResponseDTO.getExercise_type());
        if (info != null && !info.isEmpty()) {
            Double met = findMetFromCsv(info, newIntensity);
            if (met != null) return met;
        }
        
        // CSV에 변경된 강도와 같은 운동이 존재하지 않을 시 기본 MET 값 반환
        return getDefaultMet(newIntensity);
    }

    // CSV에 변경된 강도와 같은 운동이 존재 시 MET 값 반환
    private Double findMetFromCsv(List<Map<String,Object>> info, String intensity) {
        return info.stream()
            .filter(map -> intensity.equalsIgnoreCase((String) map.get("intensity")))
            .map(map -> (Double) map.get("exercise_met"))
            .findFirst()
            .orElse(null);
    }

    // CSV에 변경된 강도와 같은 운동이 존재하지 않을 시 강도에 따라 default MET 값 반환    
    private Double getDefaultMet(String intensity) {
        switch(intensity.toUpperCase()) {
            case "LOW": return 3.0;
            case "GENERAL": return 5.0;
            case "HIGH": return 7.0;
            default: return 5.0;
        }
    }

    // 운동 시간 변경 여부에 따라 반환
    private Integer calculateDuration(ExerResponseDTO exerResponseDTO, Map<String,Object> updateExerInfo) {
        return exerResponseDTO.getDuration().equals(updateExerInfo.get("duration")) 
            ? exerResponseDTO.getDuration() 
            : (Integer) updateExerInfo.get("duration");
    }
    // 변경된 값에 맞춰 칼로리 계산
    private Integer calculateCalories(Double met, Double weight, Integer duration) {
        return (int) ((met * 3.5 * weight * duration) / 1000 * 5);
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
            // 수정 성공
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            // 수정 실패
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 삭제
    // 일일 운동 조회를 통해 exercise_id를 프론트가 가지고 있는 상태
    @DeleteMapping("/delete/exer")
    public ResponseEntity<Integer> exerDelete(@RequestParam(name = "exercise_id") Integer exercise_id){
        System.out.println("class endPoint >> " + "/record/exercise/delete/exer");
        System.out.println("exercise_id >> " + exercise_id);
        int result = exerService.exerDeleteRow(exercise_id);
        System.out.println("result >> " + result);
        if(result == 1){
            // 삭제 성공
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            // 삭제 실패
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    // 운동 세트 삭제
    // 세트 정보 조회를 통해 set_id를 프론트가 가지고 있는 상태
    @DeleteMapping("/delete/set")
    public ResponseEntity<Integer> setDelete(@RequestParam(name = "set_id") Integer set_id){
        System.out.println("class endPoint >> " + "/record/exercise/delete/set");
        System.out.println("set_id >> " + set_id);    
        int result = exerService.setDeleteRow(set_id);
        System.out.println("result >> " + result);
        if(result == 1){
            // 삭제 성공
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            // 삭제 실패
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    // 특정회원의 일일 운동 별 칼로리 총합 출력 
    @GetMapping("/get/exerCalories")
    public ResponseEntity<Object> exerCaloriesGet(@RequestParam(name = "record_date") String record_date,
                                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        System.out.println("class endPoint >> " + "/record/exercise/get/exerCalories");
        
        Map<String,Object> map = new HashMap<>();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try{
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                Integer member_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
                
                System.out.println("debug >>> token : " + token);
                System.out.println("debug >>> member_id : " + member_id);

                map.put("member_id", member_id);
                map.put("record_date", record_date);
                System.out.println("map >> " + map);

                List<Map<String,Object>> list = exerService.exerCaloriesGetRow(map);
                System.out.println("exer list >> " + list);
                if(!list.isEmpty()){
                    // 값이 존재할 경우
                    return new ResponseEntity<>(list, HttpStatus.OK);
                } else {
                    // 값이 없을 경우
                    return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
                }
            } catch(Exception e){
                // 서버 에러
                System.out.println("서버 오류 발생: " + e.getMessage());
                return new ResponseEntity<>("내부 오류", HttpStatus.INTERNAL_SERVER_ERROR);            }
        } else {
            // 인증 실패
            return new ResponseEntity<>("인증 실패",HttpStatus.UNAUTHORIZED);
        }
    }

    // 특정 달 운동 기록 날짜 조회
    @PostMapping("/get/month")
    public ResponseEntity<Object> getMonthExer(@RequestBody Map<String,Object> date,
                                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        System.out.println("class endPoint >> " + "/record/exercise/get/month");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            try{
                String token = authHeader.substring(7); // "Bearer " 접두사 제거
                Integer member_id = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
                
                System.out.println("debug >>> token : " + token);
                System.out.println("debug >>> member_id : " + member_id);

                date.put("member_id",member_id);
                System.out.println("map >> " + date);

                List<Map<String,Object>> result = exerService.getMonthExerRow(date);
                System.out.println("result >> " + result);
                if(!result.isEmpty()){
                    // 값이 있을 때
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    // 값이 없을 때
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                }
            } catch(Exception e){
                // 서버 에러
                System.out.println("서버 오류 발생: " + e.getMessage());
                return new ResponseEntity<>("내부 오류", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
        // 인증 실패
        return new ResponseEntity<>("인증 실패",HttpStatus.UNAUTHORIZED);
        }
    }
}
