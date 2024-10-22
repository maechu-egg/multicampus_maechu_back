package com.multipjt.multi_pjt.badge.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.CrewBadgeResponseDTO;

@Mapper
public interface CrewBadgeMapper {
    
    //뱃지 생성
    public void insertBadge(CrewBadgeRequestDTO param);

    // 특정 회원의 뱃지 조회
     public CrewBadgeResponseDTO selectBadgeByMemberId(@Param("member_id") int memberId);
 
     // 특정 회원의 뱃지 업데이트
     public void updateBadge(CrewBadgeRequestDTO param);
}