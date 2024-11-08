package com.multipjt.multi_pjt.crew.dao.battle;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.BattleMemberResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewVoteRequestDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewVoteResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleFeedResponseDTO;
import com.multipjt.multi_pjt.crew.domain.battle.CrewBattleResponseDTO;

@Mapper
public interface CrewBattleMapper {
    // <---- 크루 배틀 ---->

    // 배틀 생성
    public void createCrewBattleRow(CrewBattleRequestDTO params);
    
    // 배틀 목록 조회
    public List<CrewBattleResponseDTO> selectCrewBattleRow(Integer crew_id);

    // 특정 배틀 상세 조회
    public CrewBattleResponseDTO selectCrewBattleDetailRow(Integer battle_id);

    // 사용자 참여 배틀 조회
    public List<CrewBattleResponseDTO> selectMyBattleRow(int member_id);

    // 배틀 참가
    public void insertBattleMemberRow(BattleMemberRequestDTO params);

    // <---- 크루 피드보기 ---->

    // 배틀 참가 멤버 조회
    public List<BattleMemberResponseDTO> selectBattleMemberRow(Integer battle_id);

    // 피드 작성
    public void createCrewBattleFeedRow(CrewBattleFeedRequestDTO params);

    // 피드 조회
    public List<CrewBattleFeedResponseDTO> selectCrewBattleFeedRow(Integer param);

    // 투표
    public void createVoteRow(CrewVoteRequestDTO params);

    // <---- 스케줄러 ---->

    // 모든 배틀 조회
    public List<CrewBattleResponseDTO> selectAllCrewBattleRow();

    // 배틀 상태 변경
    public void updateBattleState(CrewBattleRequestDTO params);

    // 배틀 삭제
    public void deleteBattleById(int battle_id);

    // 크루 멤버 승리 포인트 업데이트
    public void updateWinnerPointsRow(int member_id);

    // 크루 배지 승리 포인트 업데이트
    public void updateBadgeWinnerPointsRow(int member_id);

    // 특정 배틀에 대한 모든 투표 조회
    public List<CrewVoteResponseDTO> selectCrewVoteRow(int battle_id);
} 
