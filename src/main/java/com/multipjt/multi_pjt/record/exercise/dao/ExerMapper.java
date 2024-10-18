package com.multipjt.multi_pjt.record.exercise.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.record.exercise.domain.ExerRequestDTO;

@Mapper
public interface ExerMapper {

    public int exerInsert(ExerRequestDTO exerRequestDTO);

    public int exerDelete(Map<String,Object> map);

//    public Long exerIdGet(Map<String,Object>) map;

}
