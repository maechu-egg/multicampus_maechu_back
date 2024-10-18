package com.multipjt.multi_pjt.record;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.multipjt.multi_pjt.record.diet.dao.DietMapper;
import com.multipjt.multi_pjt.record.diet.domain.DietRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemResponseDTO;
import com.multipjt.multi_pjt.record.diet.domain.MealType;

@SpringBootTest
@Transactional
public class DietMapperApplicationTest {
    
    @Autowired
    private DietMapper dietMapper;

    ItemRequestDTO itemRequestDTO = new ItemRequestDTO();

    private Long test(){

         //given : DietRequestDTO에 임의 값 입력
         DietRequestDTO dietRequestDTO = new DietRequestDTO();
         dietRequestDTO.setMemberId(1L);
         dietRequestDTO.setMealType(MealType.BREAKFAST);
         //when : db에 값 insert
         int diet = dietMapper.dietInsert(dietRequestDTO);
 
         //then : sql 쿼리 결과 반환
         Assertions.assertThat(diet).isEqualTo(1);
         
         //given : member 1이 테스트하는 날짜의 아침에 먹은 식단 기록하기 위한 환경 조성
         dietRequestDTO.setRecordDate(LocalDate.of(2024,10,18));
         Long dietNumber = dietMapper.findDietNumber(dietRequestDTO);
 
         Assertions.assertThat(dietNumber).isNotNull();
 
         itemRequestDTO.setDietId(dietNumber);
         itemRequestDTO.setCalories(300);
         itemRequestDTO.setCarbs(30);
         itemRequestDTO.setFat(30);
         itemRequestDTO.setItemName("공기밥");
         itemRequestDTO.setQuantity(100);
         itemRequestDTO.setProtein(30);
         //when : db에 값 insert
         int item = dietMapper.itemInsert(itemRequestDTO);
 
         //then : sql 쿼리 결과 반환
         Assertions.assertThat(item).isEqualTo(1);

         return dietNumber;
    }



    @Test
    @DisplayName("001 : 식단 기입 테스트")
    public void testDietRecord(){

        test();

    }

    @Test
    @DisplayName("002 : 식단 삭제")
    public void testDietDelete(){
        //givne : 식단 기입
        Long dietNumber = test();
        //when : 한끼 식단 삭제
        int deletRow = dietMapper.dietDelete(dietNumber);
        //then : 검증
        Assertions.assertThat(deletRow).isEqualTo(1);
    }

    @Test
    @DisplayName("003 : 식단 조회")
    public void testDietFind(){
        //given : 식단 기입
        Long dietNumber = test();
        //when : 한끼 식단 조회
        List<ItemResponseDTO> list = dietMapper.itemFindAll(dietNumber);
        System.out.println("debug >>> " + list);
        //then : 검증
        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list).isNotNull();    
   
        // HashMap을 사용하여 리스트 항목을 저장, 교차 검증
        HashMap<Long, String> itemMap = new HashMap<>();
        for (ItemResponseDTO i : list) {
            itemMap.put(i.getDietId(), i.getItemName());
        }

        // HashMap 출력
        itemMap.forEach((key, value) -> {
            System.out.println("Item ID: " + key + ", Item Name: " + value);
        });
    }
    
    @Test
    @DisplayName("004 : 식품 삭제")
    public void testItemDelete(){
        //given : 식단 기입
        Long dietNumber = test();
        //given : 삭제할 식품 dietID, ItemName 임시 지정
        Map<String,Object> itemMap = new HashMap<>();
        itemMap.put("dietId",dietNumber);
        itemMap.put("itemName",itemRequestDTO.getItemName());
        //then : 검증
        int deleteNum = dietMapper.itemDelete(itemMap);
        Assertions.assertThat(deleteNum).isEqualTo(1);   
    }

    @Test
    @DisplayName("005 : 식품 수정")
    public void testItemUpdate(){
        //given : 식단 기입
        Long dietNumber = test();
        //given : 수정할 식품 dietId, ItemName 임시 지정
        Map<String,Object> itemMap = new HashMap<>();
        itemMap.put("dietId",dietNumber);
        itemMap.put("itemName",itemRequestDTO.getItemName());
        //given : 수정 사항 임시 지정
        itemRequestDTO.setItemName("잡곡밥");
        itemRequestDTO.setQuantity(200);
        //when : Map에 모든 item 속성 기입
        itemMap.put("calories",itemRequestDTO.getCalories());
        itemMap.put("carbs",itemRequestDTO.getCarbs());
        itemMap.put("fat",itemRequestDTO.getFat());
        itemMap.put("quantity",itemRequestDTO.getQuantity());
        itemMap.put("protein",itemRequestDTO.getProtein());

        //then : 검증
        int item = dietMapper.itemUpdate(itemMap);
        
        Assertions.assertThat(item).isEqualTo(1);           
    }    
}

