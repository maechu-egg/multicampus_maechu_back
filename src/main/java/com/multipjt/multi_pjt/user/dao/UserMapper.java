package com.multipjt.multi_pjt.user.dao;

import com.multipjt.multi_pjt.user.domain.UserRequestDTO;
import com.multipjt.multi_pjt.user.domain.UserResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserMapper {


    // 회원가입 (사용자 등록) - 삽입된 행 수 반환
    int registerUser(UserRequestDTO user);

    //회원 정보 수정
    int updateUser(UserRequestDTO user);

    // ID로 사용자 조회
    UserResponseDTO getUserById(@Param("member_id") String memberId);

    // 회원 삭제
    int deleteUser(String memberId);  
}

