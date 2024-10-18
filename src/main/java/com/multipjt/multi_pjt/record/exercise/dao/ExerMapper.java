package com.multipjt.multi_pjt.record.exercise.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.record.exercise.domain.ExerRequestDTO;
import com.multipjt.multi_pjt.record.exercise.domain.ExerResponseDTO;

@Mapper
public interface ExerMapper {

    public int exerInsert(ExerRequestDTO exerRequestDTO);

    public int exerDelete(Long exerId);

    public Long exerIdGet(Map<String,Object> map);

    public int exerUpdate(Map<String,Object> map);

    public List<ExerResponseDTO> exerDaySelect(Map<String,Object> map);
}
