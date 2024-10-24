package com.multipjt.multi_pjt.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multipjt.multi_pjt.jwt.JwtTokenProvider;
import com.multipjt.multi_pjt.user.domain.CustomUserDetails;
import com.multipjt.multi_pjt.user.domain.login.UserRequestDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // JwtTokenProvider 주입

    private String token;

    @BeforeEach
    public void setUp() {
        // 테스트용 CustomUserDetails 객체 생성
        CustomUserDetails userDetails = new CustomUserDetails(17, "test@example.com", "password", new ArrayList<>());
        // JWT 토큰 생성
        token = jwtTokenProvider.createAccessToken(userDetails);
        System.out.println("debug >>> token : " + token);
    }

    @Test
    @DisplayName("토큰 발급되어 헤더에 추가 후 추가 요청 테스트")
    public void testAuthenticatedRequest() throws Exception {
        mockMvc.perform(post("/api/user/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) // JWT 토큰을 Authorization 헤더에 추가
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK 응답 확인
                .andExpect(content().string("인증 성공! 사용자: test@example.com")); // 사용자 이메일에 맞는 응답 내용 확인
    }

    @Test
    @DisplayName("헤더에 토큰 없이 인증안된 사용자 요청 테스트")
    public void testUnauthorizedRequest() throws Exception {
        mockMvc.perform(post("/api/user/test")
            .contentType(MediaType.APPLICATION_JSON)) // Authorization 헤더 없이 요청
            .andExpect(status().isUnauthorized()) // HTTP 401 UNAUTHORIZED 응답 확인
            .andExpect(content().json("{\"message\": \"인증 실패\"}")); // JSON 형식의 인증 실패 메시지 확인
    }

    @Test
    @DisplayName("회원가입 테스트(인증이 필요없음)")
    public void testRegister() throws Exception {
        // 회원가입 요청 데이터 생성
        UserRequestDTO userRequest = UserRequestDTO.builder()
                .nickname("testUser3")
                .password("password123")
                .email("test3@example.com")
                .phone("01012345678")
                .build();

        // ObjectMapper를 사용하여 DTO를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userRequest);

        // 회원가입 API 호출 및 결과 검증
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"회원가입 성공\"}")); // 기대하는 JSON 형식
    }
}
