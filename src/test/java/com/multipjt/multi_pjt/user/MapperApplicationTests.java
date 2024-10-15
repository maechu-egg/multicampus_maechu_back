package com.multipjt.multi_pjt.user;

import com.multipjt.multi_pjt.user.dao.UserMapper;
import com.multipjt.multi_pjt.user.domain.UserRequestDTO;
import com.multipjt.multi_pjt.user.domain.UserResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
//@Transactional  // 테스트 후 데이터 롤백
public class MapperApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("001 : 회원가입 테스트")
    public void userRegistrationTest() {
        // Given: 회원가입에 필요한 사용자 정보 준비
        UserRequestDTO request = new UserRequestDTO();
        request.setMember_id("testuser5");
        request.setMember_nickname("Test User5");
        request.setMember_password("securepassword");
        request.setMember_email("testuser@example.com");
        request.setMember_phone("010-1234-5678");
        request.setMember_img("default.png");  // 기본 이미지 설정
        request.setMember_type("local");       // 로컬 사용자 설정

        // When: 회원가입 동작 수행
        int rowsAffected = userMapper.registerUser(request);
        Assertions.assertEquals(1, rowsAffected, "한 명의 사용자가 등록되어야 합니다.");

        // Then: 데이터베이스에서 등록된 사용자 정보 조회 및 검증
        UserResponseDTO registeredUser = userMapper.getUserById("testuser5");
        Assertions.assertNotNull(registeredUser, "회원가입에 성공해야 합니다.");
        Assertions.assertEquals("testuser5", registeredUser.getMember_id(), "사용자 ID가 일치해야 합니다.");
        Assertions.assertEquals("Test User5", registeredUser.getMember_nickname(), "닉네임이 일치해야 합니다.");
        Assertions.assertEquals("testuser@example.com", registeredUser.getMember_email(), "이메일이 일치해야 합니다.");
        Assertions.assertEquals("010-1234-5678", registeredUser.getMember_phone(), "전화번호가 일치해야 합니다.");
        Assertions.assertEquals("default.png", registeredUser.getMember_img(), "기본 이미지가 일치해야 합니다.");
        Assertions.assertEquals("local", registeredUser.getMember_type(), "회원 유형이 일치해야 합니다.");
    }

    @Test
    @DisplayName("002 : 회원 정보 수정 테스트")
    public void updateUserTest() {
        // Given: 회원가입에 필요한 초기 데이터 생성
        UserRequestDTO initialRequest = new UserRequestDTO();
        initialRequest.setMember_id("testuser6");
        initialRequest.setMember_nickname("OldNick46");
        initialRequest.setMember_password("oldpassword");
        initialRequest.setMember_email("old@example.com");
        initialRequest.setMember_phone("010-1234-5678");
        initialRequest.setMember_img("default.png");
        initialRequest.setMember_type("local");

        // 회원가입 수행
        userMapper.registerUser(initialRequest);

        // When: 회원 정보 수정
        UserRequestDTO updateRequest = new UserRequestDTO();
        updateRequest.setMember_id("testuser6");  // 동일 ID로 수정
        updateRequest.setMember_password("newpassword123");
        updateRequest.setMember_nickname("NewNick46");  // 닉네임 변경
        updateRequest.setMember_email("new@example.com");
        updateRequest.setMember_phone("010-9876-5432");
        updateRequest.setMember_img("updated.png");
        updateRequest.setMember_type("local");

        // 회원 정보 수정 수행
        int rowsAffected = userMapper.updateUser(updateRequest);
        Assertions.assertEquals(1, rowsAffected, "회원 정보가 정상적으로 수정되어야 합니다.");

        // Then: 수정된 회원 정보 검증
        UserResponseDTO updatedUser = userMapper.getUserById("testuser6");
        Assertions.assertNotNull(updatedUser, "수정된 회원 정보를 조회할 수 있어야 합니다.");
        Assertions.assertEquals("newpassword123", updatedUser.getMember_password(), "비밀번호가 수정되어야 합니다.");
        Assertions.assertEquals("NewNick46", updatedUser.getMember_nickname(), "닉네임이 수정되어야 합니다.");
        Assertions.assertEquals("new@example.com", updatedUser.getMember_email(), "이메일이 수정되어야 합니다.");
        Assertions.assertEquals("010-9876-5432", updatedUser.getMember_phone(), "전화번호가 수정되어야 합니다.");
        Assertions.assertEquals("updated.png", updatedUser.getMember_img(), "이미지가 수정되어야 합니다.");
        Assertions.assertEquals("local", updatedUser.getMember_type(), "타입이 수정되어야 합니다.");
    }

    @Test 
    @DisplayName("003 : Id로 회원 정보 조회 ")
    public void selectUserTest() {
        // Given: 회원가입에 필요한 초기 데이터 생성
        UserRequestDTO initialRequest = new UserRequestDTO();
        initialRequest.setMember_id("testuser45");
        initialRequest.setMember_nickname("nickname45");
        initialRequest.setMember_password("password45");
        initialRequest.setMember_email("old@example.com");
        initialRequest.setMember_phone("010-1234-5678");
        initialRequest.setMember_img("default.png");
        initialRequest.setMember_type("local");

        // 회원가입 수행
        userMapper.registerUser(initialRequest);

        //When : Id로 회원 정보 조회
        UserResponseDTO result = userMapper.getUserById("testuser45");

        //Then : 회원 정보 검증 
        Assertions.assertNotNull(result, "회원 정보를 조회할 수 있어야 합니다.");
        Assertions.assertEquals("testuser45", result.getMember_id(), "사용자 ID가 일치해야 합니다.");
        Assertions.assertEquals("nickname45", result.getMember_nickname(), "닉네임이 일치해야 합니다.");
        Assertions.assertEquals("old@example.com", result.getMember_email(), "이메일이 일치해야 합니다.");
        Assertions.assertEquals("010-1234-5678", result.getMember_phone(), "번호가 일치해야 합니다.");
        Assertions.assertEquals("default.png", result.getMember_img(), "사진이 일치해야 합니다.");
        Assertions.assertEquals("local", result.getMember_type(), "타입이 일치해야 합니다.");


    }

    @Test 
    @DisplayName("004 : 회원 탈퇴 테스트")
    public void deleteUserTest() {
        // Given: 회원가입에 필요한 초기 데이터 생성
        UserRequestDTO initialRequest = new UserRequestDTO();
        initialRequest.setMember_id("testuser7");
        initialRequest.setMember_nickname("nickname7");
        initialRequest.setMember_password("password7");
        initialRequest.setMember_email("user7@example.com");
        initialRequest.setMember_phone("010-1234-5678");
        initialRequest.setMember_img("default.png");
        initialRequest.setMember_type("local");

        // 회원가입 수행
        userMapper.registerUser(initialRequest);

        // When: 회원 탈퇴 수행
        int rowsAffected = userMapper.deleteUser("testuser7");
        Assertions.assertEquals(1, rowsAffected, "한 명의 사용자가 정상적으로 탈퇴되어야 합니다.");

        // Then: 탈퇴된 회원 정보 조회 시 null 확인
        UserResponseDTO deletedUser = userMapper.getUserById("testuser7");
        Assertions.assertNull(deletedUser, "탈퇴한 회원의 정보는 조회할 수 없어야 합니다.");
    }

    

    
}

