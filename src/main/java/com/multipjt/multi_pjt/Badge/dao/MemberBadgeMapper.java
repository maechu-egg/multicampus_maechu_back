package com.multipjt.multi_pjt.Badge.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.multipjt.multi_pjt.Badge.domain.badge.MemberBadgeRequestDTO;
import com.multipjt.multi_pjt.Badge.domain.badge.MemberBadgeResponseDTO;

@Mapper
public interface MemberBadgeMapper {
     // 뱃지 생성
     public void insertBadge(MemberBadgeRequestDTO param);
    
     // 특정 회원의 뱃지 조회
     public MemberBadgeResponseDTO selectBadgeByMemberId(@Param("member_id") int memberId);
 
     // 특정 회원의 뱃지 업데이트
     public void updateBadge(MemberBadgeRequestDTO param);
}
    
