package com.multipjt.multi_pjt.badge.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeResponseDTO;

@Mapper
public interface MemberBadgeMapper {
    // 뱃지 생성
    public void insertBadge(MemberBadgeRequestDTO param);
    
    // 특정 회원의 뱃지 조회
    public MemberBadgeResponseDTO getBadgeByMemberId(@Param("member_id") int memberId);

    // 특정 회원의 뱃지 업데이트
    public void updateBadge(MemberBadgeRequestDTO param);

    // 멤버 활동 기록을 조회하는 메서드
    public List<Map<String, Object>> getMemberActivity(@Param("member_id") int memberId);

    // 활동 기록을 UserActivityRecord 테이블에 삽입하는 메서드
    public void insertUserActivityRecord(Map<String, Object> activityData);
}
