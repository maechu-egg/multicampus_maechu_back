package com.multipjt.multi_pjt.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import com.multipjt.multi_pjt.user.domain.mypage.ProfileRequestDTO;
import com.multipjt.multi_pjt.user.domain.mypage.ProfileResponseDTO;

@Mapper
public interface ProfileMapper {
    //회원의 프로필 등록
    int registerProfile(ProfileRequestDTO profileRequestDTO);

    // ID로 사용자 프로필 조회
    ProfileResponseDTO getUserById(@Param("member_id") int memberId);

     //회원 프로필 수정
    int updateProfile(ProfileRequestDTO profileRequestDTO);

    // 회원 탈퇴 시 프로필 삭제
    int deleteProfileByMemberId(int memberId);
}
