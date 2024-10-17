package com.multipjt.multi_pjt.record;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;

import com.multipjt.multi_pjt.record.diet.dao.DietMapper;
import com.multipjt.multi_pjt.record.diet.domain.DietRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.ItemRequestDTO;
import com.multipjt.multi_pjt.record.diet.domain.MealType;

@SpringBootTest
@Transactional
public class DietMapperApplicationTest {
    
    @Autowired
    private DietMapper dietMapper;

    @Test
    @DisplayName("001 : 식단 기입 테스트")
    public void testDietRecord(){

         //given : DietRequestDTO에 임의 값 입력
        DietRequestDTO dietRequestDTO = new DietRequestDTO();
        dietRequestDTO.setMemberId(1L);
        dietRequestDTO.setMealType(MealType.BREAKFAST);
        //when : db에 값 insert
        int diet = dietMapper.dietInsert(dietRequestDTO);

        //then : sql 쿼리 결과 반환
        Assertions.assertThat(diet).isEqualTo(1);
        
        //given : member 1이 2024-10-18 아침에 먹은 식단 기록하기 위한 환경 조성
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
    }

}
