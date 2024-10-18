package com.multipjt.multi_pjt.record;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.multipjt.multi_pjt.record.exercise.dao.ExerMapper;
import com.multipjt.multi_pjt.record.exercise.domain.ExerRequestDTO;
import com.multipjt.multi_pjt.record.exercise.domain.Intensity;

@SpringBootTest
@Transactional
public class ExerciseMapperApplicationTest {
    
    @Autowired
    public ExerMapper exerMapper;

    ExerRequestDTO exerRequestDTO = new ExerRequestDTO();

    private void test(){

        exerRequestDTO.setExerciseType("레그프레스");
        exerRequestDTO.setIntensity(Intensity.HIGH);
        exerRequestDTO.setMemberId(1L);
        exerRequestDTO.setDuration(30);
        int row = exerMapper.exerInsert(exerRequestDTO);

        Assertions.assertThat(row).isEqualTo(1);

    }

    @Test
    @DisplayName("test001 : ExerciseRecords Insert")
    public void testExerciseRecord(){

        test();

    }



    @Test
    @DisplayName("test002 : ExerciseRecords Delete")
    public void testExerciseDelete(){

        test();

        Map<String,Object> map = new HashMap<>();
        map.put("recordDate", LocalDate.of(2024, 10, 18));
        map.put("memberId", exerRequestDTO.getMemberId());
        map.put("exerciseType", exerRequestDTO.getExerciseType());

        int row = exerMapper.exerDelete(map);

        Assertions.assertThat(row).isEqualTo(1);
    }
}
