package com.multipjt.multi_pjt.badge.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeRequestDTO;
import com.multipjt.multi_pjt.badge.domain.badge.MemberBadgeResponseDTO;
import com.multipjt.multi_pjt.badge.domain.record.UserActivityRecordResponseDTO;

@Mapper
public interface MemberBadgeMapper {
    // 뱃지 생성
    public void insertBadge(MemberBadgeRequestDTO badgeRequest);
    
    // 특정 회원의 뱃지 조회
    public MemberBadgeResponseDTO getBadgeByMemberId(@Param("member_id") int memberId);

    // 특정 회원의 뱃지 업데이트
    public void updateBadge(MemberBadgeRequestDTO param);

    // 멤버 활동 기록을 조회하는 메서드
    public List<UserActivityRecordResponseDTO> getMemberActivity(@Param("member_id") int memberId);

    // 모든 회원의 뱃지 조회
    public List<MemberBadgeResponseDTO> getAllBadges();

    BigDecimal getCurrentPoints(@Param("member_id") int memberId);

    // posts 테이블에서 활동 가져오기
    List<Map<String, Object>> getActivitiesFromPosts(int memberId);

    // comments 테이블에서 활동 가져오기
    List<Map<String, Object>> getActivitiesFromComments(int memberId);


    // 특정 회원의 특정 뱃지 조회
    public MemberBadgeResponseDTO getBadgeIdPoint(@Param("member_id") int memberId);
}

