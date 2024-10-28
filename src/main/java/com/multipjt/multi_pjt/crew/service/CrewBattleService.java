package com.multipjt.multi_pjt.crew.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipjt.multi_pjt.crew.dao.battle.CrewBattleMapper;
import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewVoteRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedResponseDTO;

@Service
public class CrewBattleService {
    @Autowired
    private CrewBattleMapper crewBattleMapper;

    // <---- 크루 배틀 ---->

    // 배틀 생성
    public void createCrewBattle(CrewBattleRequestDTO params) {
        System.out.println("debug>>> Service: createCrewBattle + " + crewBattleMapper);
        System.out.println("debug>>> Service: createCrewBattle + " + params);
        crewBattleMapper.createCrewBattleRow(params);
    }

    // 배틀 목록 조회
    public List<CrewBattleResponseDTO> selectCrewBattle(Integer crew_id) {
        System.out.println("debug>>> Service: selectCrewBattle + " + crewBattleMapper);
        return crewBattleMapper.selectCrewBattleRow(crew_id);
    }

    // 배틀 참가
    public void createBattleMember(BattleMemberRequestDTO params) {
        System.out.println("debug>>> Service: createBattleMember + " + crewBattleMapper);
        System.out.println("debug>>> Service: createBattleMember + " + params);
        crewBattleMapper.createBattleMemberRow(params);
    }

    // <---- 크루 배틀 상세보기 ---->

    // 배틀 참가 멤버 조회
    public List<BattleMemberResponseDTO> selectBattleMember(Integer battle_id) {
        System.out.println("debug>>> Service: selectBattleMember + " + crewBattleMapper);
        return crewBattleMapper.selectBattleMemberRow(battle_id);
    }

    // 피드 작성
    public void createCrewBattleFeed(CrewBattleFeedRequestDTO params) {
        System.out.println("debug>>> Service: createCrewBattleFeed + " + crewBattleMapper);
        System.out.println("debug>>> Service: createCrewBattleFeed + " + params);
        crewBattleMapper.createCrewBattleFeedRow(params);
    }

    // 피드 조회
    public List<CrewBattleFeedResponseDTO> selectCrewBattleFeed(Integer param) {
        System.out.println("debug>>> Service: selectCrewBattleFeed + " + crewBattleMapper);
        return crewBattleMapper.selectCrewBattleFeedRow(param);
    }

    // 투표
    public void createVote(CrewVoteRequestDTO params) {
        System.out.println("debug>>> Service: createVote + " + crewBattleMapper);
        System.out.println("debug>>> Service: createVote + " + params);
        crewBattleMapper.createVoteRow(params);
    }
}