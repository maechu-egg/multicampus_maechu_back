package com.multipjt.multi_pjt.user.dao;

import com.multipjt.multi_pjt.user.domain.UserRequestDTO;
import com.multipjt.multi_pjt.user.domain.UserResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {

    // 모든 사용자 조회
    List<UserResponseDTO> findAllRow();

    // 회원가입 (사용자 등록) - 삽입된 행 수 반환
    int registerUser(UserRequestDTO user);

    // ID로 사용자 조회
    UserResponseDTO getUserById(@Param("member_id") String memberId);
}

