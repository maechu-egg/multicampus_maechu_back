package com.multipjt.multi_pjt.crew;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.multipjt.multi_pjt.crew.dao.crew.CrewMapper;
import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;
import com.multipjt.multi_pjt.crew.domain.crew.CrewResponseDTO;

import java.util.List;

@SpringBootTest
// @Transactional
public class MybatisApplicationTests {
    
    @Autowired
    private CrewMapper crewMapper;

    @Test
    @DisplayName("001 : 크루 생성 테스트")
    public void createCrewTest(){
        //given : 크루 생성에 필요한 크루 정보
        CrewRequestDTO request = CrewRequestDTO.builder()
                                    .crew_id(5)
                                    .member_id("testuser")
                                    .crew_name("testCrew")
                                    .crew_goal("다이어트")
                                    .crew_title("다이어트 목표 크루원 모집")
                                    .crew_location("익산")
                                    .crew_sport("런닝")
                                    .crew_gender("혼성")
                                    .crew_frequency("주 3회")
                                    .crew_age("20대")
                                    .crew_date("2024-10-15 17:25:10")
                                    .build();
        //when : 크루 생성
        crewMapper.createCrewRow(request);

        //then : 데이터베이스에서 등록된 크루 검증
        List<CrewResponseDTO> createdCrew = crewMapper.selectCrewRow();
        System.out.println("조회된 크루 : "+ createdCrew);
        //조회된 크루 : []
        assertThat(createdCrew).isNotNull()        .withFailMessage("크루가 생성되어야 합니다.");
        assertThat(createdCrew.size()).isGreaterThan(0).withFailMessage("크루가 생성되어야 합니다."); // 크루가 생성되었는지 확인

        // // 첫 번째 크루에 대한 검증
        // CrewResponseDTO crew = createdCrew.get(0);
        // assertThat(crew.getMember_id())     .isEqualTo("testuser")                .withFailMessage("크루 생성자가 일치해야 합니다.");
        // assertThat(crew.getCrew_name())     .isEqualTo("testCrew")                .withFailMessage("크루 이름이 일치해야 합니다.");
        // assertThat(crew.getCrew_goal())     .isEqualTo("다이어트")                 .withFailMessage("크루 목표가 일치해야 합니다.");
        // assertThat(crew.getCrew_title())    .isEqualTo("다이어트 목표 크루원 모집") .withFailMessage("크루 제목이 일치해야 합니다.");
        // assertThat(crew.getCrew_location()) .isEqualTo("익산")                    .withFailMessage("크루 위치가 일치해야 합니다.");
        // assertThat(crew.getCrew_sport())    .isEqualTo("런닝")                    .withFailMessage("크루 스포츠가 일치해야 합니다.");
        // assertThat(crew.getCrew_gender())   .isEqualTo("혼성")                    .withFailMessage("크루 성별이 일치해야 합니다.");
        // assertThat(crew.getCrew_frequency()).isEqualTo("주 3회")                  .withFailMessage("크루 빈도가 일치해야 합니다.");
        // assertThat(crew.getCrew_age())      .isEqualTo("20대")                    .withFailMessage("크루 연령대가 일치해야 합니다.");
        // assertThat(crew.getCrew_date())     .isEqualTo("2024-10-15 17:25:10")     .withFailMessage("크루 날짜가 일치해야 합니다.");
    }
}
