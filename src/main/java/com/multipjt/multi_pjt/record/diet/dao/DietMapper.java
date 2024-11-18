package com.multipjt.multi_pjt.record.diet.dao;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.record.diet.domain.DietRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.DietResponseDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemResponseDTO;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
@Mapper
public interface DietMapper {
    
    // DietRecords 삽입
    public int dietInsert(Map<String,Object> map);
    // DietItems 삽입
    public int itemInsert(ItemRequestDTO requestDTO);
    // diet_id 찾기
    public Integer findDietNumber(Map<String,Object> map);
    // 특정 meal DietRecords 출력
    public DietResponseDTO dietFind(Map<String,Object> map);

    // DietRecords 출력
    public List<DietResponseDTO> dietFindAll(Map<String,Object> map);

    // DietItems 출력
    public ArrayList<ItemResponseDTO> itemFindAll(Integer dietId);
    // DietRecords 삭제
    public int dietDelete(Integer dietId);
    // DietItems 삭제
    public int itemDelete(Integer item_id);
    //DietItems 수정
    public int itemUpdate(ItemRequestDTO requestDTO);

    public ArrayList<Map<String,Object>> mealNutCheck(Map<String,Object> map);

//    public Map<String,String> getMemberInfo(Integer memberId);

    public int mealUpdate(Map<String,Object> map);

    public List<Map<String,Object>> getMonthDiet(Map<String,Object> map);

    public Map<String,Object> memberDataGet(Integer memberId);
}
