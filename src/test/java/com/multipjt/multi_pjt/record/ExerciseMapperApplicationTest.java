package com.multipjt.multi_pjt.record;

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

    @Test
    @DisplayName("test001 : ExerciseRecords")
    public void testExerciseRecord(){

        ExerRequestDTO exerRequestDTO = new ExerRequestDTO();
        exerRequestDTO.setExerciseType("레그프레스");
        exerRequestDTO.setIntensity(Intensity.HIGH);
        exerRequestDTO.setMemberId(1L);
        exerRequestDTO.setDuration(30);
        int row = exerMapper.exerInsert(exerRequestDTO);

        Assertions.assertThat(row).isEqualTo(1);
    }

}
