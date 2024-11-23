package com.multipjt.multi_pjt.record;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.multipjt.multi_pjt.record.diet.dao.DietMapper;
import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemResponseDTO;
import com.multipjt.multi_pjt.record.diet.domain.MealType;

@SpringBootTest
@Transactional
public class DietMapperApplicationTest {
    
    @Autowired
    private DietMapper dietMapper;

    ItemRequestDTO itemRequestDTO = new ItemRequestDTO();

    private Integer test(){

         //given : DietRequestDTO에 임의 값 입력
         Map<String,Object> map = new HashMap<>();
         map.put("member_id",1);
         map.put("meal_type",MealType.BREAKFAST);
         //when : db에 값 insert
         int diet = dietMapper.dietInsert(map);
 
         //then : sql 쿼리 결과 반환
         Assertions.assertThat(diet).isEqualTo(1);
         
         //given : member 1이 테스트하는 날짜의 아침에 먹은 식단 기록하기 위한 환경 조성
        map.put("record_date",LocalDate.of(2024,10,22));
 
        Integer diet_id = dietMapper.findDietNumber(map);
 
        Assertions.assertThat(diet_id).isNotNull();
 
         itemRequestDTO.setDiet_id(diet_id);
         itemRequestDTO.setCalories(300);
         itemRequestDTO.setCarbs(30D);
         itemRequestDTO.setFat(30D);
         itemRequestDTO.setItem_name("공기밥");
         itemRequestDTO.setQuantity(100);
         itemRequestDTO.setProtein(30D);
         //when : db에 값 insert
         int item = dietMapper.itemInsert(itemRequestDTO);
 
         //then : sql 쿼리 결과 반환
         Assertions.assertThat(item).isEqualTo(1);

         return diet_id;
    }



    @Test
    @DisplayName("001 : 식단 기입 테스트")
    public void testDietRecord(){

        test();

    }

    @Test
    @DisplayName("002 : 한끼 식단 삭제")
    public void testDietDelete(){
        //givne : 식단 기입
        Integer dietNumber = test();
        //when : 한끼 식단 삭제
        int deletRow = dietMapper.dietDelete(dietNumber);
        //then : 검증
        Assertions.assertThat(deletRow).isEqualTo(1);
    }

    @Test
    @DisplayName("003 : 한끼 식단 식품 조회")
    public void testDietFind(){
        //given : 식단 기입
        Integer dietNumber = test();
        //when : 한끼 식단 조회
        List<ItemResponseDTO> list = dietMapper.itemFindAll(dietNumber);
        System.out.println("debug >>> " + list);
        //then : 검증
        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list).isNotNull();    
   
        // HashMap을 사용하여 리스트 항목을 저장, 교차 검증
        HashMap<Integer, String> itemMap = new HashMap<>();
        for (ItemResponseDTO i : list) {
            itemMap.put(i.getDiet_id(), i.getItem_name());
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
        Integer diet_id = test();
        //given : 삭제할 식품 dietID, ItemName 임시 지정
        Map<String,Object> itemMap = new HashMap<>();
        itemMap.put("diet_id",diet_id);
        itemMap.put("item_name",itemRequestDTO.getItem_name());
        //then : 검증
        int deleteNum = dietMapper.itemDelete(diet_id);
        Assertions.assertThat(deleteNum).isEqualTo(1);   
    }

    @Test
    @DisplayName("005 : 식품 수정") 
    public void testItemUpdate(){
        //given : 식단 기입
        Integer dietNumber = test();
        //given : 수정할 식품 dietId, ItemName 임시 지정
        ItemRequestDTO itemRequestDTO = new ItemRequestDTO();
        itemRequestDTO.setDiet_id(dietNumber);
        itemRequestDTO.setItem_name(itemRequestDTO.getItem_name());
        itemRequestDTO.setCalories(300);
        itemRequestDTO.setCarbs(30D);
        itemRequestDTO.setFat(30D);
        itemRequestDTO.setQuantity(200);
        itemRequestDTO.setProtein(30D);
        //then : 검증
        int item = dietMapper.itemUpdate(itemRequestDTO);
        
        Assertions.assertThat(item).isEqualTo(1);           
    }    

    @Test
    @DisplayName("006 : 특정 회원이 특정 날짜에 식사 타입별로 섭취한 영양성분 조회")
    public void testItemCheckNutr(){
        // 식사타입 breakfast
        //given : DietRequestDTO에 임의 값 입력
        Map<String,Object> map = new HashMap<>();
        map.put("member_id",1);
        map.put("meal_type",MealType.BREAKFAST);
       //when : db에 값 insert
        int diet = dietMapper.dietInsert(map);
 
        //then : sql 쿼리 결과 반환
        Assertions.assertThat(diet).isEqualTo(1);
         
        //given : member 1이 테스트하는 날짜의 아침에 먹은 식단 기록하기 위한 환경 조성
        map.put("record_date",LocalDate.of(2024,10,22));
 
        Integer dietNumber = dietMapper.findDietNumber(map);
 
        Assertions.assertThat(dietNumber).isNotNull();
        
        itemRequestDTO.setDiet_id(dietNumber);
        itemRequestDTO.setCalories(300);
        itemRequestDTO.setCarbs(30D);
        itemRequestDTO.setFat(30D);
        itemRequestDTO.setItem_name("공기밥");
        itemRequestDTO.setQuantity(100);
        itemRequestDTO.setProtein(30D);
        //when : db에 값 insert
        int item = dietMapper.itemInsert(itemRequestDTO);
 
        //then : sql 쿼리 결과 반환
        Assertions.assertThat(item).isEqualTo(1);

        // 식사타입 lunch
        Map<String,Object> map2 = new HashMap<>();
        map2.put("member_id",1);
        map2.put("meal_type",MealType.LUNCH);
       //when : db에 값 insert
        int diet2 = dietMapper.dietInsert(map2);
 
        //then : sql 쿼리 결과 반환
        Assertions.assertThat(diet2).isEqualTo(1);
         
        //given : member 1이 테스트하는 날짜의 아침에 먹은 식단 기록하기 위한 환경 조성
        Map<String,Object> map3 = new HashMap<>();
        map3.put("member_id", 1);
        map3.put("meal_type", MealType.LUNCH);
        map3.put("record_date",LocalDate.of(2024,10,22));
 
        Integer dietNumber2 = dietMapper.findDietNumber(map2);
 
        Assertions.assertThat(dietNumber2).isNotNull();
 
        itemRequestDTO.setDiet_id(dietNumber2);
        itemRequestDTO.setCalories(300);
        itemRequestDTO.setCarbs(30D);
        itemRequestDTO.setFat(30D);
        itemRequestDTO.setItem_name("공기밥");
        itemRequestDTO.setQuantity(100);
        itemRequestDTO.setProtein(30D);
        //when : db에 값 insert
        int item2 = dietMapper.itemInsert(itemRequestDTO);

        //then : sql 쿼리 결과 반환
        Assertions.assertThat(item2).isEqualTo(1);

        // 식사타입 dinner인 DietRecords만 생성, dinner의 Item는 없음
        //given : DietRequestDTO에 임의 값 입력
        Map<String,Object> map4 = new HashMap<>();
        map4.put("member_id",1);
        map4.put("meal_type",MealType.DINNER);
       //when : db에 값 insert
        int diet3 = dietMapper.dietInsert(map4);
 
        //then : sql 쿼리 결과 반환
        Assertions.assertThat(diet3).isEqualTo(1);

        //then : 검증
        Map<String,Object> type = new HashMap<>();
        type.put("member_id", 1);
        type.put("record_date",LocalDate.of(2024,10,22));
        List<Map<String, Object>> list = dietMapper.mealNutCheck(type);
        Assertions.assertThat(list).size().isEqualTo(2);
    }
}