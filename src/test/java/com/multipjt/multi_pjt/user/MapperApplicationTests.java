package com.multipjt.multi_pjt.user;

import com.multipjt.multi_pjt.user.dao.UserMapper;
import com.multipjt.multi_pjt.user.domain.login.UserRequestDTO;
import com.multipjt.multi_pjt.user.domain.login.UserResponseDTO;
import com.multipjt.multi_pjt.user.domain.login.UserUpdateRequestDTO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional  // 테스트 후 데이터 롤백
public class MapperApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("001 : 회원가입 테스트")
    public void userRegistrationTest() {
        // Given: 회원가입에 요청 데이터 
        UserRequestDTO user = new UserRequestDTO(
            0,                          // member_id (기본값으로 0 또는 다른 적절한 값)
            "default.jpg",             // member_img
            "testUser",                // nickname
            "password123",             // password
            "test@example.com",        // email
            "010-1234-5678",          // phone
            true,                      // verified
            "kakao",                   // snsProvider
            null  
        );


        // When: 회원가입 동작 수행
        int rowsAffected = userMapper.registerUser(user);
       
        // Then: 데이터베이스에서 등록된 사용자 정보 조회 및 검증
        Assertions.assertEquals(1, rowsAffected, "한 명의 사용자가 등록되어야 합니다.");

        UserResponseDTO insertedUser = userMapper.getUserByEmail("test@example.com");
        Assertions.assertNotNull(insertedUser, "사용자가 데이터베이스에 존재해야 합니다.");
        Assertions.assertEquals("testUser", insertedUser.getNickname(), "닉네임이 일치해야 합니다.");


        
    }

    @Test
@DisplayName("002 : 회원 정보 수정 테스트")
public void updateUserTest() {
    // Given: 새로운 회원 가입을 위한 요청 데이터
    UserRequestDTO newUser = new UserRequestDTO(
        0,                          // member_id (기본값으로 0 또는 다른 적절한 값)
        "default.jpg",              // member_img
        "testUser",                 // nickname
        "password123",              // password
        "test@example.com",         // email
        "010-1234-5678",            // phone
        true,                       // verified
        "kakao",                    // snsProvider
        null                        // 기타 추가 필드
    );

    // 새로운 회원 가입
    int rowsAffected = userMapper.registerUser(newUser);
    Assertions.assertEquals(1, rowsAffected, "한 명의 사용자가 등록되어야 합니다.");

    // When: 수정할 회원 정보 설정
    UserUpdateRequestDTO updatedUser = new UserUpdateRequestDTO();
    updatedUser.setMemberId(newUser.getMemberId());  // 회원 ID 설정
    updatedUser.setNickname("updatedUser");          // 수정할 닉네임
    updatedUser.setPhoneNumber("010-9876-5432");     // 수정할 전화번호
    updatedUser.setMemberImg("updated.jpg");         // 수정할 이미지

    // // 회원 정보 수정 수행
    // int updateRowsAffected = userMapper.updateUser(updatedUser);
    // Assertions.assertEquals(1, updateRowsAffected, "회원 정보가 성공적으로 수정되어야 합니다.");

    // // Then: 수정된 정보 조회 및 검증
    // UserResponseDTO retrievedUser = userMapper.getUserByEmail("test@example.com");
    // Assertions.assertNotNull(retrievedUser, "수정된 사용자가 조회되어야 합니다.");
    // Assertions.assertEquals("updatedUser", retrievedUser.getNickname(), "닉네임이 일치해야 합니다.");
    // Assertions.assertEquals("010-9876-5432", retrievedUser.getPhone(), "전화번호가 일치해야 합니다.");
    // Assertions.assertEquals("updated.jpg", retrievedUser.getMemberImg(), "이미지 파일명이 일치해야 합니다.");
}

    @Test 
    @DisplayName("003 : email 회원 정보 조회 ")
    public void selectUserTest() {
        // Given: 새로운 회원 가입을 위한 요청 데이터
        UserRequestDTO newUser = new UserRequestDTO(
            0,                          // member_id (기본값으로 0 또는 다른 적절한 값)
            "default.jpg",             // member_img
            "testUser",                // nickname
            "password123",             // password
            "test@example.com",        // email
            "010-1234-5678",          // phone
            true,                      // verified
            "kakao",                   // snsProvider
            null  
        );

        // 새로운 회원 가입
        int rowsAffected = userMapper.registerUser(newUser);
        Assertions.assertEquals(1, rowsAffected, "한 명의 사용자가 등록되어야 합니다.");

        // When: 등록된 사용자의 이메일로 정보 조회
        UserResponseDTO retrievedUser = userMapper.getUserByEmail("test@example.com");
        Assertions.assertNotNull(retrievedUser, "사용자가 데이터베이스에 존재해야 합니다.");

        // Then: 조회된 정보 검증
        Assertions.assertEquals("testUser", retrievedUser.getNickname(), "닉네임이 일치해야 합니다.");
        Assertions.assertEquals("test@example.com", retrievedUser.getEmail(), "이메일이 일치해야 합니다.");
        Assertions.assertEquals("010-1234-5678", retrievedUser.getPhone(), "전화번호가 일치해야 합니다.");
     }

    @Test 
    @DisplayName("004 : 회원 탈퇴 테스트")
    public void deleteUserTest() {
         // Given: 새로운 회원 가입을 위한 요청 데이터
         UserRequestDTO newUser = new UserRequestDTO(
            0,                          // member_id (기본값으로 0 또는 다른 적절한 값)
            "default.jpg",             // member_img
            "testUser",                // nickname
            "password123",             // password
            "test@example.com",        // email
            "010-1234-5678",          // phone
            true,                      // verified
            "kakao",                   // snsProvider
            null  
        );

        // 새로운 회원 가입
        int rowsAffected = userMapper.registerUser(newUser);
        Assertions.assertEquals(1, rowsAffected, "한 명의 사용자가 등록되어야 합니다.");

        // When: 등록된 사용자의 이메일로 정보 조회하여 회원 ID 가져오기
        UserResponseDTO registeredUser = userMapper.getUserByEmail("test@example.com");
        Assertions.assertNotNull(registeredUser, "사용자가 데이터베이스에 존재해야 합니다.");
        
        Integer memberId = registeredUser.getMemberId();

        // 회원 탈퇴 수행
        int deleteRowsAffected = userMapper.deleteUserById(memberId);
        Assertions.assertEquals(1, deleteRowsAffected, "회원이 성공적으로 탈퇴되어야 합니다.");

        // Then: 데이터베이스에서 사용자 정보 확인
        UserResponseDTO deletedUser = userMapper.getUserByEmail("test@example.com");
        Assertions.assertNull(deletedUser, "회원 정보는 삭제되어야 합니다."); // 삭제된 경우 null이어야 함
    }
   
}

