package com.multipjt.multi_pjt.user.dao;

import com.multipjt.multi_pjt.user.domain.login.UserRequestDTO;
import com.multipjt.multi_pjt.user.domain.login.UserResponseDTO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {


    // 회원가입 (사용자 등록) - 삽입된 행 수 반환
    int registerUser(UserRequestDTO user);

    // 이메일 중복 확인 
    int existsByEmail(String email);

    // 닉네임 중복 확인
    int existsByNickname(String nickname);



    //회원 정보 수정
    void updateUser(UserRequestDTO userRequestDTO);

   // ID로 회원 조회
   UserResponseDTO getUserById(Integer member_id);  // memberId를 매개변수로 받음

    // 이메일로 사용자 조회
    UserResponseDTO getUserByEmail(@Param("email") String email);


   // 회원 ID로 사용자 삭제
   int deleteUserById(Integer userId);

   // 비밀번호 변경 
   void updateUserPassword(UserResponseDTO userResponseDTO);
   
}

