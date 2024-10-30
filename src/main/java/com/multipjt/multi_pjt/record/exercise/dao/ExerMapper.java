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

    public Long exerInsert(ExerRequestDTO exerRequestDTO);

    public int exerDelete(Long exer_id);

    public List<Long> exerIdGet(Map<String,Object> map);

    public int exerUpdate(Map<String,Object> map);

    public List<ExerResponseDTO> exerDaySelect(Map<String,Object> map);

    public int setInsert(SetRequestDTO setRequestDTO);

    public List<SetResponseDTO> getSetInfo(Long exercise_id);

    public int setUpdate(Map<String,Object> map);

    public int setDelete(Long set_id);

    public List<Map<String,Object>> exerCaloriesGet(Map<String,Object> map);

    public List<Map<String,Object>> metGet(String exercise_name);

    public Float getMemberInfo(Long member_id);
}
