package com.multipjt.multi_pjt.CrewBattle;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.multipjt.multi_pjt.crew.dao.battle.CrewBattleMapper;
import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewVoteRequestDTO;

@SpringBootTest
public class CrewBattleTest {
    
    @Autowired
    private CrewBattleMapper crewBattleMapper;

    @Test
    public void saveCrewBattleRowTest() {
        System.out.println("debug mapper >>> " + crewBattleMapper);
        CrewBattleRequestDTO request = CrewBattleRequestDTO.builder()
                                        .battle_id(4)
                                        .battle_name("1번째 배틀")
                                        .battle_goal("다이어트")
                                        .battle_end_recruitment("2024-10-15 17:25:10")
                                        .battle_end_date("2024-11-18 00:00:00")
                                        .battle_content("몸무게 및 칼로리 감량")
                                        .battle_state(0)
                                        .crew_id(2)
                                        .build();
        crewBattleMapper.saveCrewBattleRow(request);
        System.out.println("debug >>> save success!!!");
    }

    @Test
    public void selectCrewBattleTest() {
        System.out.println("debug mapper >>> " + crewBattleMapper);
        List<CrewBattleResponseDTO> list = crewBattleMapper.selectCrewBattleRow();
        for( CrewBattleResponseDTO dto : list) {
            System.out.println(dto);
        }
    }

    @Test
    public void saveBattleMemberTest() {
        System.out.println("debug mapper >>> " + crewBattleMapper);
        BattleMemberRequestDTO request = BattleMemberRequestDTO.builder()
                                            .participant_id(3)
                                            .battle_id(1)
                                            .member_id(1)
                                            .build();
        crewBattleMapper.saveBattleMemberRow(request);
    }

    @Test
    public void selectBattleMemberTest() {
        System.out.println("debug mapper >>> " + crewBattleMapper);
        List<BattleMemberResponseDTO> list = crewBattleMapper.selectBattleMemberRow();
        for( BattleMemberResponseDTO dto : list) {
            System.out.println(dto);
        }
    }

    @Test
    public void saveCrewBattleFeedTest() {
        System.out.println("debug mapper >>> " + crewBattleMapper);
        CrewBattleFeedRequestDTO request = CrewBattleFeedRequestDTO.builder()
                                            .feed_id(4)
                                            .feed_img("달리기 사진")
                                            .feed_post("달리기 했어용")
                                            .feed_kcal(500)
                                            .feed_sport("달리기")
                                            .feed_time(1)
                                            .participant_id(3)
                                            .build();
        crewBattleMapper.saveCrewBattleFeedRow(request);
    }

    @Test
    public void selectCrewBattleFeedTest() {
        System.out.println("debug mapper >>> " + crewBattleMapper);
        List<CrewBattleFeedResponseDTO> list = crewBattleMapper.selectCrewBattleFeedRow();
        for( CrewBattleFeedResponseDTO dto : list) {
            System.out.println(dto);
        }
    }

    @Test
    public void saveVoteTest() {
        System.out.println("debug mapper >>> " + crewBattleMapper);
        CrewVoteRequestDTO request = CrewVoteRequestDTO.builder()
                                        .vote_id(2)
                                        .battle_id(1)
                                        .participant_id(1)
                                        .member_id(1)
                                        .build();
        crewBattleMapper.saveVoteRow(request);
    }
}
