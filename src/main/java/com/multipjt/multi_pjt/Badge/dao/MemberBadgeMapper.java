package com.multipjt.multi_pjt.badge.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeResponseDTO;

@Mapper
public interface MemberBadgeMapper {
     // 뱃지 생성
     public void insertBadge(MemberBadgeRequestDTO param);
    
     // 특정 회원의 뱃지 조회
     public MemberBadgeResponseDTO getselectBadgeByMemberId(@Param("member_id") int memberId);
 
     // 특정 회원의 뱃지 업데이트
     public void updateBadge(MemberBadgeRequestDTO param);

    
}
    
