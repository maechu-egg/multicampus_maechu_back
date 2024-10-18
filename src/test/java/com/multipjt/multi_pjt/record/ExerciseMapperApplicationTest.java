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

import com.multipjt.multi_pjt.record.exercise.dao.ExerMapper;
import com.multipjt.multi_pjt.record.exercise.domain.ExerRequestDTO;
import com.multipjt.multi_pjt.record.exercise.domain.ExerResponseDTO;
import com.multipjt.multi_pjt.record.exercise.domain.Intensity;

@SpringBootTest
@Transactional
public class ExerciseMapperApplicationTest {
    
    @Autowired
    public ExerMapper exerMapper;

    ExerRequestDTO exerRequestDTO = new ExerRequestDTO();

    private void test(){
        //given : ExerRequestDTO에 임의 값 지정
        exerRequestDTO.setExerciseType("레그프레스");
        exerRequestDTO.setIntensity(Intensity.HIGH);
        exerRequestDTO.setMemberId(1L);
        exerRequestDTO.setDuration(30);
        //when : ExerciseRecords에 값 Insert
        int row = exerMapper.exerInsert(exerRequestDTO);

        //then : 검증
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

        //given : 운동 번호를 알기 위해 날짜, 멤버번호, 운동종목으로 필터링함
        Map<String,Object> map = new HashMap<>();
        map.put("recordDate", LocalDate.of(2024, 10, 19));
        map.put("memberId", exerRequestDTO.getMemberId());
        map.put("exerciseType", exerRequestDTO.getExerciseType());
        Long exerciseId = exerMapper.exerIdGet(map);

        //when : 운동번호를 통해 해당 운동기록을 삭제
        int row = exerMapper.exerDelete(exerciseId);

        //then : 검증
        Assertions.assertThat(row).isEqualTo(1);
    }

    @Test
    @DisplayName("test003 : ExerciseRecords Update")
    public void testExerciseUpdate(){

        test();

        //given : 운동 번호를 알기 위해 날짜, 멤버번호, 운동종목으로 필터링함
        Map<String,Object> map = new HashMap<>();
        map.put("recordDate", LocalDate.of(2024, 10, 19));
        map.put("memberId", exerRequestDTO.getMemberId());
        map.put("exerciseType", exerRequestDTO.getExerciseType());

        Long exerciseId = exerMapper.exerIdGet(map);
        //given : 변경된 값 임의 지정
        map.put("exerciseId", exerciseId);
        map.put("intensity",Intensity.HIGH);
        //when : 변경된 값을 Update
        int row = exerMapper.exerUpdate(map);
        //then : 검증
        Assertions.assertThat(row).isEqualTo(1);
    }

    @Test
    @DisplayName("test004 : 일일 ExerciseRecords SELECT")
    public void testDayExerciseSelect(){
        //given : 임의 값 ExerciseRecords에 Insert
        ExerRequestDTO exerRequestDTO1 = new ExerRequestDTO();
        exerRequestDTO1.setExerciseType("레그컬");
        exerRequestDTO1.setIntensity(Intensity.HIGH);
        exerRequestDTO1.setMemberId(1L);
        exerRequestDTO1.setDuration(30);

        ExerRequestDTO exerRequestDTO2 = new ExerRequestDTO();
        exerRequestDTO2.setExerciseType("레그프레스");
        exerRequestDTO2.setIntensity(Intensity.HIGH);
        exerRequestDTO2.setMemberId(1L);
        exerRequestDTO2.setDuration(30);

        ExerRequestDTO exerRequestDTO3 = new ExerRequestDTO();
        exerRequestDTO3.setExerciseType("레그프레스");
        exerRequestDTO3.setIntensity(Intensity.HIGH);
        exerRequestDTO3.setMemberId(17L);
        exerRequestDTO3.setDuration(30);

        int row1 = exerMapper.exerInsert(exerRequestDTO1);
        Assertions.assertThat(row1).isEqualTo(1);

        int row2 = exerMapper.exerInsert(exerRequestDTO2);
        Assertions.assertThat(row2).isEqualTo(1);

        int row3 = exerMapper.exerInsert(exerRequestDTO3);
        Assertions.assertThat(row3).isEqualTo(1);

        Map<String,Object> map = new HashMap<>();
        map.put("memberId",1L);
        map.put("recordDate",LocalDate.of(2024, 10, 19));

        //when : memberId와 Date로 회원이 특정 날짜에 한 운동 기록 조회        
        List<ExerResponseDTO> list = exerMapper.exerDaySelect(map);
        
        Assertions.assertThat(list).size().isEqualTo(2);            
    }
}
