package com.multipjt.multi_pjt.record.diet.dao;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.record.diet.domain.DietRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;

@Mapper
public interface DietMapper {
    
    public int dietInsert(DietRequestDTO requestDTO);

    public int itemInsert(ItemRequestDTO requestDTO);
 
    public Long findDietNumber(DietRequestDTO requestDTO);
}
