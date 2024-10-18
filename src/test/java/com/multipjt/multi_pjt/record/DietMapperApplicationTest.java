package com.multipjt.multi_pjt.record;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

import com.multipjt.multi_pjt.record.diet.dao.DietMapper;
import com.multipjt.multi_pjt.record.diet.domain.DietRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemResponseDTO;
import com.multipjt.multi_pjt.record.diet.domain.MealType;

@SpringBootTest
public class DietMapperApplicationTest {
    
    @Autowired
    private DietMapper dietMapper;

    @Test
    @DisplayName("001 : 식단 기입 테스트")
    public Long testDietRecord(){

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

        ItemRequestDTO itemRequestDTO = new ItemRequestDTO();
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
    @DisplayName("002 : 식단 삭제")
    public void testDietDelete(){
    
        Long dietNumber = testDietRecord();

        int deletRow = dietMapper.dietDelete(dietNumber);

        Assertions.assertThat(deletRow).isEqualTo(1);
    }

    @Test
    @DisplayName("003 : 식단 조회")
    public void testDietFind(){
    
        Long dietId = testDietRecord();

        List<ItemResponseDTO> list = dietMapper.itemFindAll(dietId);
        System.out.println("debug >>> " + list);

        Assertions.assertThat(list).isNotEmpty();

        // 특정 항목에 대한 검증 (예: 첫 번째 항목 확인)
        Assertions.assertThat(list.get(0).getDietId()).isEqualTo(dietId);
        Assertions.assertThat(list.get(0).getItemName()).isEqualTo("공기밥");

    }
    
    @Test
    @DisplayName("004 : 식품 삭제")
    public void testItemDelete(){

        ItemRequestDTO itemRequestDTO = new ItemRequestDTO();
        itemRequestDTO.setDietId(1L);
        itemRequestDTO.setCalories(300);
        itemRequestDTO.setCarbs(30);
        itemRequestDTO.setFat(30);
        itemRequestDTO.setItemName("공기밥");
        itemRequestDTO.setQuantity(100);
        itemRequestDTO.setProtein(30);
        //when : db에 값 insert
        int item = dietMapper.itemInsert(itemRequestDTO);

        Assertions.assertThat(item).isEqualTo(1);

        dietMapper.itemDelete(null);

    }

}
