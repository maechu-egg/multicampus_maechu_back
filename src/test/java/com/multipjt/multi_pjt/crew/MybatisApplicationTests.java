package com.multipjt.multi_pjt.crew;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.multipjt.multi_pjt.crew.dao.crew.CrewMapper;
import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;

@SpringBootTest
@Transactional
public class MybatisApplicationTests {
    
    @Autowired
    private CrewMapper crewMapper;

    @Test
    public void createCrewTest(){
        //given : 크루 생성에 필요한 크루 정보
        CrewRequestDTO request = CrewRequestDTO.builder()
                                    .crew_id(3)
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
        //then : 크루 
        crewMapper.createCrew(request);
    }
}
