package com.multipjt.multi_pjt.user;

import com.multipjt.multi_pjt.user.dao.UserMapper;
import com.multipjt.multi_pjt.user.domain.UserRequestDTO;
import com.multipjt.multi_pjt.user.domain.UserResponseDTO;
import org.junit.jupiter.api.Assertions;
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
    public void userRegistrationTest() {
        // Given: 회원가입에 필요한 사용자 정보 준비
        UserRequestDTO request = new UserRequestDTO();
        request.setMember_id("testuser2");
        request.setMember_nickname("Test User2");
        request.setMember_password("securepassword");
        request.setMember_email("testuser@example.com");
        request.setMember_phone("010-1234-5678");
        request.setMember_img("default.png");  // 기본 이미지 설정
        request.setMember_type("local");       // 로컬 사용자 설정

        // When: 회원가입 동작 수행
        int rowsAffected = userMapper.registerUser(request);
        Assertions.assertEquals(1, rowsAffected, "한 명의 사용자가 등록되어야 합니다.");

        // Then: 데이터베이스에서 등록된 사용자 정보 조회 및 검증
        UserResponseDTO registeredUser = userMapper.getUserById("testuser2");
        Assertions.assertNotNull(registeredUser, "회원가입에 성공해야 합니다.");
        Assertions.assertEquals("testuser2", registeredUser.getMember_id(), "사용자 ID가 일치해야 합니다.");
        Assertions.assertEquals("Test User2", registeredUser.getMember_nickname(), "닉네임이 일치해야 합니다.");
        Assertions.assertEquals("testuser@example.com", registeredUser.getMember_email(), "이메일이 일치해야 합니다.");
        Assertions.assertEquals("010-1234-5678", registeredUser.getMember_phone(), "전화번호가 일치해야 합니다.");
        Assertions.assertEquals("default.png", registeredUser.getMember_img(), "기본 이미지가 일치해야 합니다.");
        Assertions.assertEquals("local", registeredUser.getMember_type(), "회원 유형이 일치해야 합니다.");
    }
}

