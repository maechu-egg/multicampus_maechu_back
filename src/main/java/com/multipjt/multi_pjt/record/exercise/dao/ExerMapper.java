package com.multipjt.multi_pjt.record.exercise.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.record.exercise.domain.ExerRequestDTO;
import com.multipjt.multi_pjt.record.exercise.domain.ExerResponseDTO;
import com.multipjt.multi_pjt.record.exercise.domain.SetRequestDTO;
import com.multipjt.multi_pjt.record.exercise.domain.SetResponseDTO;

@Mapper
public interface ExerMapper {

    public int exerInsert(ExerRequestDTO exerRequestDTO);

    public int exerDelete(Long exerId);

    public List<Long> exerIdGet(Map<String,Object> map);

    public int exerUpdate(Map<String,Object> map);

    public List<ExerResponseDTO> exerDaySelect(Map<String,Object> map);

    public Map<String,Integer> getTotalCaloriesByExerciseType(Map<String,Object> map);

    public int setInsert(SetRequestDTO setRequestDTO);

    public SetResponseDTO getSetInfo(Long exerciseId);

    public int setUpdate(Map<String,Object> map);

    public int setDelete(Long setId);
}
