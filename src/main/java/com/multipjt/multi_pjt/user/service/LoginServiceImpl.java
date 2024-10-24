package com.multipjt.multi_pjt.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import com.multipjt.multi_pjt.jwt.EmailProvider;
import com.multipjt.multi_pjt.jwt.JwtTokenProvider;
import com.multipjt.multi_pjt.user.dao.UserMapper;
import com.multipjt.multi_pjt.user.domain.CustomUserDetails;
import com.multipjt.multi_pjt.user.domain.login.UserRequestDTO;
import com.multipjt.multi_pjt.user.domain.login.UserResponseDTO;
import com.multipjt.multi_pjt.user.domain.login.EmailCertificationCodeDTO;
import com.multipjt.multi_pjt.user.domain.login.LoginDTO;

import java.util.ArrayList;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service 
public class LoginServiceImpl implements UserDetailsService { // UserDetailsService 구현

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserMapper userMapper; // UserMapper 주입

    @Autowired
    private PasswordEncoder passwordEncoder; // PasswordEncoder 주입

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailProvider emailProvider;

    private final ConcurrentHashMap<String, EmailCertificationCodeDTO> emailCertificationMap = new ConcurrentHashMap<>(); // 이메일과 인증 코드를 저장할 Map

    private final SecureRandom random = new SecureRandom();

    // 1. 회원가입 메서드 추가
    public void registerUser(UserRequestDTO userRequestDTO) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
        userRequestDTO.setPassword(encodedPassword); // 암호화된 비밀번호로 설정
        userMapper.registerUser(userRequestDTO);
    }

    //1.2 이메일 형식 검증 메서드 추가
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"; // 간단한 이메일 정규 표현식
        return email.matches(emailRegex);
    }

    // 1.3 회원가입 시 이메일 유효성 확인 
    public boolean existsByEmail(String email) {
        int count = userMapper.existsByEmail(email);
        return count > 0; //0보다 크면 중복 이메일 존재 
    }

    // 1.4 회원가입 시 닉네임 중복 확인 
    public boolean existsByNickname(String nickname) {
        int count = userMapper.existsByNickname(nickname);
        return count > 0; 
    }

    // 1.5 이메일 인증 코드 발송 
    public boolean sendCertificationEmail(String email) {
        String certificationNumber = generateCertificationNumber();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5); // 5분 후 만료

        boolean result = emailProvider.sendCertificationMail(email, certificationNumber);
        
        if (result) {
            emailCertificationMap.put(email, new EmailCertificationCodeDTO(email, certificationNumber, expiryTime));
            return true;
        } else {
            // 로깅 추가
            log.error("Failed to send certification email to: {}", email);
            return false;
        }
    }

    public String generateCertificationNumber() {
        StringBuilder certificationNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            certificationNumber.append(random.nextInt(10));
        }
        return certificationNumber.toString();
    }

    // 인증 코드 확인 메서드 추가
    public boolean verifyCertificationCode(String email, String code) {
        EmailCertificationCodeDTO certification = emailCertificationMap.get(email);
        if (certification == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(certification.getExpiryTime())) {
            emailCertificationMap.remove(email);
            return false;
        }

        boolean isValid = certification.getCertificationCode().equals(code);
        if (isValid) {
            emailCertificationMap.remove(email); // 사용된 코드 제거
        }
        return isValid;
    }


 
    // 2. 로그인 : 이메일, 비번 가져와 검증 후 존재하면 jwt 토큰 생성 
    public String login(LoginDTO loginDTO) {
        // 이메일로 사용자 조회
        UserResponseDTO user = userMapper.getUserByEmail(loginDTO.getEmail());
        System.out.println("loginDTO.getEmail" + loginDTO.getEmail());

        // 사용자 존재 여부 확인
        if (user == null) {
            throw new RuntimeException("Invalid username or password"); // 사용자 없음
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password"); // 비밀번호 불일치
        }

        // 비밀번호가 일치하는 경우 로그인 성공 메시지 출력
        System.out.println("로그인 성공: " + user.getEmail()); // 사용자 ID 출력 (또는 다른 사용자 정보)

        // CustomUserDetails 객체 생성
        CustomUserDetails userDetails = new CustomUserDetails(
            user.getMemberId(), // memberId
            user.getEmail(), // 이메일
            user.getPassword(), // 비밀번호
            new ArrayList<>() // 권한 목록 (필요에 따라 수정)
        );

        // 비밀번호가 일치하는 경우 JWT 토큰 생성
        return jwtTokenProvider.createAccessToken(userDetails); // CustomUserDetails를 사용하여 토큰 생성
    }

    @Override
    public UserDetails loadUserByUsername(String useremail) {
        // 사용자 정보를 데이터베이스에서 가져오는 로직 구현
        UserResponseDTO user = userMapper.getUserByEmail(useremail); // 사용자 이름으로 사용자 검색
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + useremail); // 사용자 없음 예외 처리
        }

        // CustomUserDetails 객체 생성 (SecurityContext Holder에 저장)
        return new CustomUserDetails(
            user.getMemberId(), // memberId
            user.getEmail(), // 이메일
            user.getPassword(), // 비밀번호
            new ArrayList<>() // 권한 목록 (필요에 따라 수정)
        );
    }

    // @Override
    // public void expireToken(String token) {
    //     jwtTokenProvider.expireToken(token);
    // }
}
