package com.multipjt.multi_pjt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;


import org.springframework.transaction.annotation.Transactional;

import com.multipjt.multi_pjt.crew.dao.CrewMapper;
import com.multipjt.multi_pjt.crew.domain.crew.CrewRequestDTO;

@SpringBootTest
@Transactional

public class MybatisApplicationTests {
    
    @Autowired
    private CrewMapper crewMapper;

    @Test

    public void saveCrewBattleRowTest() {
        System.out.println("debug mapper >>>>> " + crewMapper);
        CrewBattleRequestDTO request = CrewBattleRequestDTO.builder()
                                            .battle_id(1)
                                            .battle_name("1번째 배틀")
                                            .battle_goal("다이어트")
                                            .battle_end_recruitment("2024-10-15 17:25:10")
                                            .battle_end_date("2024-11-18 00:00:00")
                                            .battle_content("몸무게 및 칼로리 감량")
                                            .battle_state(1)
                                            .crew_id(1)
                                            .build();
        crewMapper.saveCrewBattleRow(request);
        System.out.println("debug >>> save success!!!");
    }    
}
