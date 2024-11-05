package com.multipjt.multi_pjt.record.diet.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.record.diet.dao.DietMapper;
import com.multipjt.multi_pjt.record.diet.domain.DietRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.DietResponseDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemResponseDTO;

@Service
public class DietService {

    @Autowired
    private DietMapper dietMapper;

    public int dietInsertRow(DietRequestDTO dietRequestDTO){
        return dietMapper.dietInsert(dietRequestDTO);
    } 


    public int itemInsertRow(ItemRequestDTO itemRequestDTO){
        return dietMapper.itemInsert(itemRequestDTO);
    }

    public Integer findDietRow(Map<String,Object> map){
        return dietMapper.findDietNumber(map);
    }

    public List<DietResponseDTO> dietFindAllRow(Map<String,Object> map){
        return dietMapper.dietFindAll(map);
    }
        
    public List<ItemResponseDTO> itemFindAllRow(Integer diet_id){
        return dietMapper.itemFindAll(diet_id);
    }

    public int itemUpdateRow(ItemRequestDTO itemRequestDTO){
        return dietMapper.itemUpdate(itemRequestDTO);
    }

    public int itemDeleteRow(Integer item_id){
        return dietMapper.itemDelete(item_id);
    }

    public int deleteRecordRow(Integer diet_id){
        return dietMapper.dietDelete(diet_id);
    }

    public List<Map<String,Object>> mealNutCheckRow(Map<String,Object> map){
        return dietMapper.mealNutCheck(map);
    }

    // public Map<String,String> getMemberInfoRow(Integer memberId){
    //     return dietMapper.getMemberInfo(memberId);
    // }

    public int mealUpdateRow(DietRequestDTO dietRequestDTO){
        return dietMapper.mealUpdate(dietRequestDTO);
    }
    public List<String> getMonthDietRow(Map<String,Object> map){
        return dietMapper.getMonthDiet(map);
    }

    public Map<String,Object> calculateTdeeRow(Integer memberId){
        return dietMapper.calculateTdee(memberId);
    }
}
