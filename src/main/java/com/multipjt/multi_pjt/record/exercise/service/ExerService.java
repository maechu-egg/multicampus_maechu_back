package com.multipjt.multi_pjt.record.exercise.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.multipjt.multi_pjt.record.exercise.dao.ExerMapper;
import com.multipjt.multi_pjt.record.exercise.domain.ExerRequestDTO;
import com.multipjt.multi_pjt.record.exercise.domain.ExerResponseDTO;
import com.multipjt.multi_pjt.record.exercise.domain.SetRequestDTO;
import com.multipjt.multi_pjt.record.exercise.domain.SetResponseDTO;

@Service
public class ExerService {
    
    @Autowired
    private ExerMapper exerMapper;

    public Integer exerInsertRow(ExerRequestDTO exerRequestDTO){
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
}
