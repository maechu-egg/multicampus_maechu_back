package com.multipjt.multi_pjt.crew.dao.battle;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewVoteRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleResponseDTO;

@Mapper
public interface CrewBattleMapper {
    // <---- 크루 배틀 ---->

    // 배틀 생성
    public void createCrewBattleRow(CrewBattleRequestDTO params);
    
    // 배틀 목록 조회
    public List<CrewBattleResponseDTO> selectCrewBattleRow(CrewBattleRequestDTO params);

    // 배틀 신청
    public void createBattleMemberRow(BattleMemberRequestDTO params);

    // <---- 크루 배틀 상세보기 ---->

    // 배틀 참가 멤버 조회
    public List<BattleMemberResponseDTO> selectBattleMemberRow();

    // 피드 작성
    public void createCrewBattleFeedRow(CrewBattleFeedRequestDTO params);

    // 피드 조회
    public List<CrewBattleFeedResponseDTO> selectCrewBattleFeedRow();

    // 투표
    public void createVoteRow(CrewVoteRequestDTO params);
} 
