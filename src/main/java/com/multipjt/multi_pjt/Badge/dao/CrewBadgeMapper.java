package com.multipjt.multi_pjt.badge.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeResponseDTO;

@Mapper
public interface CrewBadgeMapper {
    
    //뱃지 생성
    public void insertBadge(CrewBadgeRequestDTO param);

     // 특정 크루원의 뱃지 정보 조회 
     public CrewBadgeResponseDTO selectCrewBadgeByMemberId(@Param("memberId") int memberId);

 
     // 특정 회원의 뱃지 업데이트
     public void updateBadge(CrewBadgeRequestDTO param);

     // 특정 회원의 배틀 승수 합산 조회
     Integer selectTotalBattleWinsByMemberId(@Param("memberId") int memberId);

     // 특정 회원의 새로운 배틀 승리 수 조회
     Integer selectNewBattleWinsByMemberId(int memberId);

     // 특정 회원의 배틀 승리 수 업데이트
     public void updateCrewBattleWins(@Param("memberId") int memberId, @Param("newBattleWins") int newBattleWins);

     // 크루 뱃지 랭킹 조회
     List<Map<String, Object>> selectCrewBadgeRanking();
}