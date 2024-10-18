package com.multipjt.multi_pjt.record.exercise.dao;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.record.exercise.domain.ExerRequestDTO;

@Mapper
public interface ExerMapper {

    public int exerInsert(ExerRequestDTO exerRequestDTO);

}
