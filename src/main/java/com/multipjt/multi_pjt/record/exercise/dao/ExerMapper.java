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

    public Integer exerInsert(ExerRequestDTO exerRequestDTO);

    public int exerDelete(Integer exer_id);

    public ExerResponseDTO exerGet(Integer exercise_id);

    public int exerUpdate(Map<String,Object> map);

    public List<ExerResponseDTO> exerDaySelect(Map<String,Object> map);

    public int setInsert(SetRequestDTO setRequestDTO);

    public List<SetResponseDTO> getSetInfo(Integer exercise_id);

    public int setUpdate(Map<String,Object> map);

    public int setDelete(Integer set_id);

    public List<Map<String,Object>> exerCaloriesGet(Map<String,Object> map);

    public List<Map<String,Object>> metGet(String exercise_name);

    public Double getMemberInfo(Integer member_id);

    public List<String> getMonthExer(Map<String,Object> map);
}
