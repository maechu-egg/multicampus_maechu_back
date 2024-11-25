package com.multipjt.multi_pjt.record.exercise.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.multipjt.multi_pjt.record.exercise.dao.ExerMapper;
import com.multipjt.multi_pjt.record.exercise.domain.ExerRequestDTO;
import com.multipjt.multi_pjt.record.exercise.domain.ExerResponseDTO;
import com.multipjt.multi_pjt.record.exercise.domain.Intensity;
import com.multipjt.multi_pjt.record.exercise.domain.SetRequestDTO;
import com.multipjt.multi_pjt.record.exercise.domain.SetResponseDTO;

@Service
public class ExerService {
    
    @Autowired
    private ExerMapper exerMapper;

    public Integer processExerciseInsertion(ExerRequestDTO exerRequestDTO) {
        // met 값 계산
        Double met = calculateMet(exerRequestDTO);
        System.out.println("debug >>> met : " + met);
    
        exerRequestDTO.setMet(met);

        // 회원의 몸무게 가져오기
        Double weight = getMemberInfoRow(exerRequestDTO.getMember_id());
        System.out.println("debug >>> weight : " + weight);


        exerRequestDTO.setCalories(calculateCalories(met, weight, exerRequestDTO.getDuration()));

        System.out.println("debug >>> calories : " + exerRequestDTO.getCalories());

        // 운동 정보 DB에 저장
        return exerInsertRow(exerRequestDTO);
    }

    // met 계산 메서드
    private Double calculateMet(ExerRequestDTO exerRequestDTO) {
        List<Map<String, Object>> info = metGetRow(exerRequestDTO.getExercise_type());
        Double met = null;
        Double tempMet = null;

        // csv에 운동 존재
        if (!info.isEmpty()) {
            for (Map<String, Object> map : info) {
                String intensity = (String) map.get("intensity");
                // csv에 존재하고 강도가 존재
                if (intensity != null && intensity.equalsIgnoreCase(exerRequestDTO.getIntensity().toString())) {
                    met = (Double) map.get("exercise_met");
                    break;
                // csv에 존재하지만 강도가 존재 x
                } else if (intensity == null) {
                    tempMet = (Double) map.get("exercise_met");
                }
            }
            // csv에는 존재하나 general 강도만 존재
            if (met == null) {
                // general 기준으로 high, low 강도 계산
                met = adjustMetForIntensity(exerRequestDTO.getIntensity(), tempMet);
            }
        // csv에 운동 존재 x
        } else {
            // 강도에 따라 default met 지정
            met = setDefaultMetByIntensity(exerRequestDTO.getIntensity());
        }
        return met;
    }

    // csv에 운동이 존재하나 genearl 강도만 존재 시 met 지정
    private Double adjustMetForIntensity(Intensity intensity, Double tempMet) {
        if (tempMet == null) {
            return setDefaultMetByIntensity(intensity);
        }

        switch (intensity) {
            case LOW:
                return tempMet / 1.5;
            case GENERAL:
                return tempMet;
            case HIGH:
                return tempMet * 1.5;
            default:
                return tempMet; // 기본값 설정
        }
    }

    // csv에 없는 운동 강도를 통해 met 지정
    private Double setDefaultMetByIntensity(Intensity intensity) {
        switch (intensity) {
            case LOW:
                return 3.0;
            case GENERAL:
                return 5.0;
            case HIGH:
                return 7.0;
            default:
                return 5.0; // 기본값 설정
        }
    }

    // met, weight, duration 을 통해 칼로리 계산
    private Integer calculateCalories(Double met, Double weight, Integer duration) {
        return (int) ((met * 3.5 * weight * duration) / 1000 * 5);
    }
    // db에 insert
    public Integer exerInsertRow(ExerRequestDTO exerRequestDTO) {
        exerMapper.exerInsert(exerRequestDTO);
        return exerRequestDTO.getExercise_id();
    }

    public ExerResponseDTO exerGetRow(Integer exercise_id){
        return exerMapper.exerGet(exercise_id);
    }

    public int setInsertRow(SetRequestDTO setRequestDTO){
        return exerMapper.setInsert(setRequestDTO);
    }

    public List<SetResponseDTO> setInfoGetRow(Integer exerciseId){
        return exerMapper.getSetInfo(exerciseId);
    }

    public List<ExerResponseDTO> exerDayGetRow(Map<String,Object> map){
        return exerMapper.exerDaySelect(map);
    }

    public int exerUpdateRow(Map<String,Object> map){
        return exerMapper.exerUpdate(map);
    }

    public int setUpdateRow(Map<String,Object> map){
        return exerMapper.setUpdate(map);
    }

    public int exerDeleteRow(Integer exerciseId){
        return exerMapper.exerDelete(exerciseId);
    }

    public int setDeleteRow(Integer setId){
        return exerMapper.setDelete(setId);
    }

    public List<Map<String,Object>> exerCaloriesGetRow(Map<String,Object> map){
        return exerMapper.exerCaloriesGet(map);
    }

    public List<Map<String,Object>> metGetRow(String exercise_name){
        return exerMapper.metGet(exercise_name);
    }

    public Double getMemberInfoRow(Integer memberId){
        return exerMapper.getMemberInfo(memberId);
    }

    public List<Map<String,Object>> getMonthExerRow(Map<String,Object> map){
        return exerMapper.getMonthExer(map);
    }
}
