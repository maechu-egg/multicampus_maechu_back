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
import com.multipjt.multi_pjt.record.exercise.domain.SetRequestDTO;
import com.multipjt.multi_pjt.record.exercise.domain.SetResponseDTO;

@SpringBootTest
@Transactional
public class ExerciseMapperApplicationTest {
    
    @Autowired
    public ExerMapper exerMapper;

    ExerRequestDTO exerRequestDTO = new ExerRequestDTO();

    private void test(){
        //given : ExerRequestDTO에 임의 값 지정
        exerRequestDTO.setExercise_type("레그프레스");
        exerRequestDTO.setIntensity(Intensity.HIGH);
        exerRequestDTO.setMember_id(1L);
        exerRequestDTO.setDuration(30);
        exerRequestDTO.setCalories(78F);
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
    @DisplayName("test002 : 운동 번호를 통해 ExerciseRecords Delete")
    public void testExerciseDelete(){

        test();

        //given : 운동 번호를 알기 위해 날짜, 멤버번호, 운동종목으로 필터링함
        Map<String,Object> map = new HashMap<>();
        map.put("recordDate", LocalDate.of(2024, 10, 22));
        map.put("memberId", exerRequestDTO.getMember_id());
        map.put("exerciseType", exerRequestDTO.getExercise_type());
        
        // 회원이 하루에 같은 운동을 n번 할 경우 운동 번호 여러 개 나오면 exerciseId의 크기가 작은 게 먼저 한 것
        List<Long> exerciseId = exerMapper.exerIdGet(map);
        System.out.println("debug >>> " + exerciseId);
        //when : 운동번호를 통해 해당 운동기록을 삭제
        int row = exerMapper.exerDelete(exerciseId.get(0));

        //then : 검증
        Assertions.assertThat(row).isEqualTo(1);
    }

    @Test
    @DisplayName("test003 : 운동 번호를 통해 ExerciseRecords Update")
    public void testExerciseUpdate(){

        test();

        //given : 운동 번호를 알기 위해 날짜, 멤버번호, 운동종목으로 필터링함
        Map<String,Object> map = new HashMap<>();
        map.put("recordDate", LocalDate.of(2024, 10, 22));
        map.put("memberId", exerRequestDTO.getMember_id());
        map.put("exerciseType", exerRequestDTO.getExercise_type());


        // 회원이 하루에 같은 운동을 n번 할 경우 운동 번호 여러 개 나오면 exerciseId의 크기가 작은 게 먼저 한 것        
        List<Long> exerciseId = exerMapper.exerIdGet(map);
        //given : 변경된 값 임의 지정
        System.out.println("debug >>> " + exerciseId);

        // 수정은 강도와 시간만 가능하게 하고 운동명은 수정 불가능하게 하자
        Map<String,Object> map2 = new HashMap<>();
        map2.put("exerciseId", exerciseId.get(0));
        map2.put("intensity",Intensity.LOW);
        map2.put("duration", exerRequestDTO.getDuration());
        map2.put("calories", exerRequestDTO.getCalories());
        // 추후에 시간과 강도로 인해 칼로리 소모값 변공되는 것도 반영해야 함

        //when : 변경된 값을 Update
        int row = exerMapper.exerUpdate(map2);
        //then : 검증
        Assertions.assertThat(row).isEqualTo(1);
    }

    @Test
    @DisplayName("test004 : 특정회원이 특정 날짜에 기록한 ExerciseRecords SELECT 및 Set")
    public void testDayExerciseSelect(){
        //given : 임의 값 ExerciseRecords에 Insert

        //memberId 1인 회원 레그컬 기록
        ExerRequestDTO exerRequestDTO1 = new ExerRequestDTO();
        exerRequestDTO1.setExercise_type("레그컬");
        exerRequestDTO1.setIntensity(Intensity.HIGH);
        exerRequestDTO1.setMember_id(1L);
        exerRequestDTO1.setDuration(30);
        exerRequestDTO1.setCalories(78F);
        //memberId 1인 회원 레그프레스 기록
        ExerRequestDTO exerRequestDTO2 = new ExerRequestDTO();
        exerRequestDTO2.setExercise_type("레그프레스");
        exerRequestDTO2.setIntensity(Intensity.HIGH);
        exerRequestDTO2.setMember_id(1L);
        exerRequestDTO2.setDuration(30);
        exerRequestDTO2.setCalories(85F);
        //memberId 17인 회원 레그프레스 기록
        ExerRequestDTO exerRequestDTO3 = new ExerRequestDTO();
        exerRequestDTO3.setExercise_type("레그프레스");
        exerRequestDTO3.setIntensity(Intensity.HIGH);
        exerRequestDTO3.setMember_id(17L);
        exerRequestDTO3.setDuration(30);
        exerRequestDTO3.setCalories(89F);

        // ExerciseRecords에 삽입 검증
        int row1 = exerMapper.exerInsert(exerRequestDTO1);
        Assertions.assertThat(row1).isEqualTo(1);

        int row2 = exerMapper.exerInsert(exerRequestDTO2);
        Assertions.assertThat(row2).isEqualTo(1);

        int row3 = exerMapper.exerInsert(exerRequestDTO3);
        Assertions.assertThat(row3).isEqualTo(1);

        // memberId, recordDate를 통해 회원이 특정 날짜에 한 운동들 조회하기 위해 설정한 값
        Map<String,Object> map = new HashMap<>();
        map.put("memberId",1L);
        map.put("recordDate",LocalDate.of(2024, 10, 22));

        //when : memberId와 Date로 회원이 특정 날짜에 한 운동 기록 조회        
        // 회원이 하루에 같은 운동을 n번 할 경우 운동 번호 여러 개 나오면 exerciseId의 크기가 작은 게 먼저 한 것        
        List<ExerResponseDTO> list = exerMapper.exerDaySelect(map);
        System.out.println("debug list >>> " + list);

        //then : 검증
        Assertions.assertThat(list).size().isEqualTo(2);       
        
        for (ExerResponseDTO exer : list) {
            Long exerciseId = exer.getExercise_id();
            System.out.println("debug exer >>> " + exer);

            Assertions.assertThat(exer.getExercise_id()).isNotNull();

            System.out.println("운동명: " + exer.getExercise_type() + ", ExerciseId: " + exerciseId);
            
            // ExerciseSet에 ExerciseId 일치하는 임의 값 insert
            SetRequestDTO setRequestDTO = new SetRequestDTO();
            setRequestDTO.setExercise_id(exerciseId);
            setRequestDTO.setRepetitions(10);
            int row = exerMapper.setInsert(setRequestDTO);

            Assertions.assertThat(row).isEqualTo(1);

            // when: 해당 운동의 Set 정보 조회
            SetResponseDTO setResponseDTO = exerMapper.getSetInfo(exerciseId);
            System.out.println("debug setResponseDTO>>> " + setResponseDTO);

            // then: Set 정보가 있는지 확인 및 출력
            Assertions.assertThat(setResponseDTO).isNotNull();
        }
    }
    @Test
    @DisplayName("test005 : 특정 운동종목의 Set Insert")
    public void testSetInfoSelect(){
       
        test();
        
        Map<String,Object> map = new HashMap<>();
        map.put("recordDate", LocalDate.of(2024, 10, 22));
        map.put("memberId", exerRequestDTO.getMember_id());
        map.put("exerciseType", exerRequestDTO.getExercise_type());
        
        // 회원이 특정 날짜에 한 특정 운동의 운동 번호 조회
        // 회원이 하루에 같은 운동을 n번 할 경우 운동 번호 여러 개 나오면 exerciseId의 크기가 작은 게 먼저 한 것       
        List<Long> exerciseId = exerMapper.exerIdGet(map);
         
        SetRequestDTO setRequestDTO = new SetRequestDTO();
        //given : ExerciseSet에 넣을 임의 값 지정
        setRequestDTO.setDistance(3F);
        setRequestDTO.setExercise_id(exerciseId.get(0));
        setRequestDTO.setRepetitions(null);
        setRequestDTO.setWeight(null);
        
        //when : ExerciseSet Insert
        int row = exerMapper.setInsert(setRequestDTO);
        //then : 검증
        Assertions.assertThat(row).isEqualTo(1);
    }
    @Test
    @DisplayName("test006 : Set Update")
    public void testSetUpdate(){

        test();
        
        Map<String,Object> map = new HashMap<>();
        map.put("recordDate", LocalDate.of(2024, 10, 22));
        map.put("memberId", exerRequestDTO.getMember_id());
        map.put("exerciseType", exerRequestDTO.getExercise_type());
        
        // 회원이 특정 날짜에 한 특정 운동의 운동 번호 조회
        // 회원이 하루에 같은 운동을 n번 할 경우 운동 번호 여러 개 나오면 exerciseId의 크기가 작은 게 먼저 한 것       
        List<Long> exerciseId = exerMapper.exerIdGet(map);

        SetRequestDTO setRequestDTO = new SetRequestDTO();
      
        setRequestDTO.setDistance(3F);
        setRequestDTO.setExercise_id(exerciseId.get(0));
        setRequestDTO.setRepetitions(null);
        setRequestDTO.setWeight(null);
        
        // ExerciseSet Insert
        int row = exerMapper.setInsert(setRequestDTO);
        // 검증
        Assertions.assertThat(row).isEqualTo(1);
        // exercise_id를 통해 set 정보 출력
        SetResponseDTO setResponseDTO = exerMapper.getSetInfo(exerciseId.get(0));
        System.out.println("debug setResponseDTO >>> " + setResponseDTO);
        //given : set_id를 통해 update할 준비
        Map<String,Object> map2 = new HashMap<>();
        map2.put("set_id", setResponseDTO.getSet_id());
        map2.put("distance", 7F);
        map2.put("weight", setResponseDTO.getWeight());
        map2.put("repetitions", setResponseDTO.getRepetitions());
        
        System.out.println("debug map2 >>> " + map2);
        //when : update
        int row2 = exerMapper.setUpdate(map2);
        //given : 검증
        Assertions.assertThat(row2).isEqualTo(1);
    }

    @Test
    @DisplayName("test007 : Set Delete")
    public void testSetDelete(){

        test();
        
        Map<String,Object> map = new HashMap<>();
        map.put("recordDate", LocalDate.of(2024, 10, 22));
        map.put("memberId", exerRequestDTO.getMember_id());
        map.put("exerciseType", exerRequestDTO.getExercise_type());
        
        // 회원이 특정 날짜에 한 특정 운동의 운동 번호 조회
        // 회원이 하루에 같은 운동을 n번 할 경우 운동 번호 여러 개 나오면 exerciseId의 크기가 작은 게 먼저 한 것       
        List<Long> exerciseId = exerMapper.exerIdGet(map);

        SetRequestDTO setRequestDTO = new SetRequestDTO();
      
        setRequestDTO.setDistance(3F);
        setRequestDTO.setExercise_id(exerciseId.get(0));
        setRequestDTO.setRepetitions(null);
        setRequestDTO.setWeight(null);
        
        // ExerciseSet Insert
        int row = exerMapper.setInsert(setRequestDTO);
        // 검증
        Assertions.assertThat(row).isEqualTo(1);
        //given : set_id를 통해 delete할 준비
        SetResponseDTO setResponseDTO = exerMapper.getSetInfo(exerciseId.get(0));
        System.out.println("debug setResponseDTO >>> " + setResponseDTO);

        Long setId = setResponseDTO.getSet_id();
      
        System.out.println("debug setId >>> " + setId);
        //when : delete
        int row2 = exerMapper.setDelete(setId);
        //then : 검증
        Assertions.assertThat(row2).isEqualTo(1);
    }
}
