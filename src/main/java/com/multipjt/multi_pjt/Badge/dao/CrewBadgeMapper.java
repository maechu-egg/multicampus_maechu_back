package com.multipjt.multi_pjt.badge.dao;

import org.apache.ibatis.annotations.Mapper;

import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeResponseDTO;

@Mapper
public interface CrewBadgeMapper {
    
    //뱃지 생성
    public void insertBadge(CrewBadgeRequestDTO param);

     // 특정 크루원의 뱃지 정보 조회 
     public CrewBadgeResponseDTO selectCrewBadgeByMemberId(int memberId);

     // 특정 회원의 배틀 승수 조회
    int selectBattleWinsByMemberId(int memberId);
 
     // 특정 회원의 뱃지 업데이트
     public void updateBadge(CrewBadgeRequestDTO param);
}