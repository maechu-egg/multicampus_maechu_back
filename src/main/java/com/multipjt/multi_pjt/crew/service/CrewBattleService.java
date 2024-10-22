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
        crewBattleMapper.createCrewBattleRow(params);
    }

    // 배틀 목록 조회
    public List<CrewBattleResponseDTO> selectCrewBattle() {
        return crewBattleMapper.selectCrewBattleRow();
    }

    // 배틀 신청
    public void createBattleMember(BattleMemberRequestDTO params) {
        crewBattleMapper.createBattleMemberRow(params);
    }

    // <---- 크루 배틀 상세보기 ---->

    // 배틀 참가 멤버 조회
    public List<BattleMemberResponseDTO> selectBattleMember() {
        return crewBattleMapper.selectBattleMemberRow();
    }

    // 피드 작성
    public void createCrewBattleFeed(CrewBattleFeedRequestDTO params) {
        crewBattleMapper.createCrewBattleFeedRow(params);
    }

    // 피드 조회
    public List<CrewBattleFeedResponseDTO> selectCrewBattleFeed() {
        return crewBattleMapper.selectCrewBattleFeedRow();
    }

    // 투표
    public void createVote(CrewVoteRequestDTO params) {
        crewBattleMapper.createVoteRow(params);
    }
}