package com.multipjt.multi_pjt.record.diet.dao;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.record.diet.domain.DietRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemResponseDTO;

import java.util.ArrayList;
import java.util.Map;
@Mapper
public interface DietMapper {
    
    // DietRecords 삽입
    public int dietInsert(DietRequestDTO requestDTO);
    // DietItems 삽입
    public int itemInsert(ItemRequestDTO requestDTO);
    // diet_id 찾기
    public Long findDietNumber(DietRequestDTO requestDTO);
    // DietItems 출력
    public ArrayList<ItemResponseDTO> itemFindAll(Long dietId);
    // DietRecords 삭제
    public int dietDelete(Long dietId);
    // DietItems 삭제
    public int itemDelete(Map<String,Object> map);
    //DietItems 수정
    public int itemUpdate(Map<String,Object> map);

}
