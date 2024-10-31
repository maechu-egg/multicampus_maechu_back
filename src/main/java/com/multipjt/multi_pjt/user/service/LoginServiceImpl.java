package com.multipjt.multi_pjt.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import com.multipjt.multi_pjt.jwt.EmailProvider;
import com.multipjt.multi_pjt.jwt.JwtTokenProvider;
import com.multipjt.multi_pjt.user.dao.UserMapper;
import com.multipjt.multi_pjt.user.domain.CustomUserDetails;
import com.multipjt.multi_pjt.user.domain.login.UserRequestDTO;
import com.multipjt.multi_pjt.user.domain.login.UserResponseDTO;
import com.multipjt.multi_pjt.user.domain.login.ChangePwDTO;
import com.multipjt.multi_pjt.user.domain.login.EmailCertificationCodeDTO;
import com.multipjt.multi_pjt.user.domain.login.LoginDTO;

import java.util.ArrayList;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<String> registerUser(UserRequestDTO userRequestDTO, MultipartFile memberImgFile) {
        // 이메일 유효성 검사
        if (!isValidEmail(userRequestDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("{\"Code\": \"INVALID_EMAIL\", \"Message\": \"유효하지 않은 이메일 형식입니다.\"}");
        }

        // 이메일 중복 확인
        if (existsByEmail(userRequestDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("{\"Code\": \"EMAIL_ALREADY_EXISTS\", \"Message\": \"이미 존재하는 이메일입니다.\"}");
        }

        // 닉네임 중복 확인
        if (existsByNickname(userRequestDTO.getNickname())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("{\"Code\": \"NICKNAME_ALREADY_EXISTS\", \"Message\": \"이미 존재하는 닉네임입니다.\"}");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
        userRequestDTO.setPassword(encodedPassword); // 암호화된 비밀번호로 설정

        // 이미지 저장 로직 추가
        if (memberImgFile != null && !memberImgFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + memberImgFile.getOriginalFilename();
            Path path = Paths.get("src/main/resources/static/" + fileName); // 정적 폴더 경로
            logger.info("Attempting to save image: {} at path: {}", fileName, path.toString()); // 파일 이름과 경로 로그

            try {
                Files.copy(memberImgFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                userRequestDTO.setMember_img(fileName); // DB에 저장할 파일 이름 설정
                logger.info("Image uploaded successfully: {}", fileName); // 성공 로그
            } catch (IOException e) {
                logger.error("Image upload failed: {}", e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body("{\"Code\": \"IMAGE_UPLOAD_FAILED\", \"Message\": \"이미지 업로드 실패: " + e.getMessage() + "\"}");
            }
        } else {
            userRequestDTO.setMember_img(null); // 이미지가 없을 경우 null 설정
        }

        // DB에 저장하기 전 UserRequestDTO 상태 로그
        logger.info("UserRequestDTO before saving to DB: {}", userRequestDTO);

        try {
            logger.info("Registering user: {}", userRequestDTO);
            userMapper.registerUser(userRequestDTO);
            logger.info("User registered successfully: {}", userRequestDTO.getEmail()); // 성공 로그
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"Code\": \"DATA_INTEGRITY_VIOLATION\", \"Message\": \"데이터 무결성 위반.\"}");
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"Code\": \"REGISTRATION_FAILED\", \"Message\": \"" + e.getMessage() + "\"}");
        }

        // 회원가입 성공 메시지를 JSON 형식으로 반환
        return ResponseEntity.ok("{\"message\": \"회원가입 성공\"}");
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

    //1.5.1 인증 코드 생성 메서드
    public String generateCertificationNumber() {
        StringBuilder certificationNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            certificationNumber.append(random.nextInt(10));
        }
        return certificationNumber.toString();
    }

    //1.5.2 인증 코드 확인 메서드 추가
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
    public ResponseEntity<Map<String, Object>> login(LoginDTO loginDTO) {
        // 이메일로 사용자 조회
        UserResponseDTO user = userMapper.getUserByEmail(loginDTO.getEmail());
        System.out.println("loginDTO.getEmail: " + loginDTO.getEmail());
        
        // 사용자 존재 여부 확인
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Collections.singletonMap("message", "이메일 혹은 비밀번호가 틀렸습니다.")); // 사용자 없음
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Collections.singletonMap("message", "이메일 혹은 비밀번호가 틀렸습니다.")); // 비밀번호 불일치
        }

        // 비밀번호가 일치하는 경우 로그인 성공 메시지 출력
        System.out.println("로그인 성공: " + user.getEmail());

        // CustomUserDetails 객체 생성
        CustomUserDetails userDetails = new CustomUserDetails(
            user.getMemberId(),
            user.getEmail(),
            user.getPassword(),
            new ArrayList<>()
        );

        // 비밀번호가 일치하는 경우 JWT 토큰 생성
        String token = jwtTokenProvider.createAccessToken(userDetails);
        
        // JSON 형식으로 토큰과 memberId 반환
        Map<String, Object> response = new HashMap<>();
        response.put("token", token); // JWT 토큰을 "token" 키로 추가
        response.put("memberId", user.getMemberId()); // memberId를 추가

        return ResponseEntity.ok(response); // JSON 형식으로 응답 반환
    }

    //3. 사용자 인증 메서드 : 이메일로 조회해 SecurityContext Holder에 저장
    @Override
    public UserDetails loadUserByUsername(String useremail) {
        // 사용자 정보를 데이터베이스에서 가져오는 로직 구현
        UserResponseDTO user = userMapper.getUserByEmail(useremail); // 사용자 이름으로 사용자 검색
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + useremail); // 사용자 없음 예외 처리
        }

        // CustomUserDetails 객 생성 (SecurityContext Holder에 저장)
        return new CustomUserDetails(
            user.getMemberId(), // memberId
            user.getEmail(), // 이메일
            user.getPassword(), // 비밀번호
            new ArrayList<>() // 권한 목록 (필요에 따라 수정)
        );
    }

    //4. 회원 탈퇴 메서드 
    public void deleteUser(int userId) {
        logger.info("Deleting user with ID: {}", userId); // userId 출력
        userMapper.deleteUserById(userId);
    }

    //5. 회원 정보 수정 메서드 
    @Transactional
    public ResponseEntity<String> updateUser(int userId, UserRequestDTO userRequestDTO) {
        // 이메일 유효성 검사
        if (!isValidEmail(userRequestDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("{\"Code\": \"INVALID_EMAIL\", \"Message\": \"유효하지 않은 이메일 형식입니다.\"}");
        }

        // 이메일 중복 확인 (자신의 이메일은 제외)
        if (existsByEmail(userRequestDTO.getEmail()) && !userRequestDTO.getEmail().equals(getCurrentUserEmail(userId))) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("{\"Code\": \"EMAIL_ALREADY_EXISTS\", \"Message\": \"이미 존재하는 이메일입니다.\"}");
        }

        // 닉네임 중복 확인 (자신의 닉네임은 제외)
        if (existsByNickname(userRequestDTO.getNickname()) && !userRequestDTO.getNickname().equals(getCurrentUserNickname(userId))) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("{\"Code\": \"NICKNAME_ALREADY_EXISTS\", \"Message\": \"이미 존재하는 닉네임입니다.\"}");
        }

        // 비밀번호가 제공된 경우 암호화
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
            userRequestDTO.setPassword(encodedPassword); // 암호화된 비밀번호로 설정
        }

        // userId 값을 member_id에 설정
        userRequestDTO.setMemberId(userId); // member_id에 userId 설정

        // 로그로 업데이트할 객체 출력
        logger.info("Updating user with ID: {}, Data: {}", userId, userRequestDTO);

        try {
            userMapper.updateUser(userRequestDTO); // 사용자 정보 업데이트
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"Code\": \"DATA_INTEGRITY_VIOLATION\", \"Message\": \"데이터 무결성 위반.\"}");
        } catch (Exception e) {
            logger.error("Update failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"Code\": \"UPDATE_FAILED\", \"Message\": \"" + e.getMessage() + "\"}");
        }

        // 새로운 토큰 생성
        CustomUserDetails userDetails = new CustomUserDetails(userId, userRequestDTO.getEmail(), userRequestDTO.getPassword(), new ArrayList<>());
        String newToken = jwtTokenProvider.createAccessToken(userDetails); // 새로운 토큰 생성

        // 회원 정보 수정 성공 메시지와 새로운 토큰을 JSON 형식으로 반환
        return ResponseEntity.ok("{\"Code\": \"OK\", \"Message\": \"회원 정보 수정 성공\", \"newToken\": \"" + newToken + "\"}");
    }

    //5.1 현재 사용자의 이메일을 가져오는 메서드 
    private String getCurrentUserEmail(int userId) {
        // userId로 현재 사용자의 이메일을 데이터베이스에서 조회하는 로직을 구현
        UserResponseDTO user = userMapper.getUserById(userId);
        
        // 로그 추가
        if (user != null) {
            logger.info("User ID1: {}, Email: {}", userId, user.getEmail());
        } else {
            logger.warn("User not found for ID: {}", userId);
        }
        logger.info("user.getEmail()1: " + user.getEmail());
        return user.getEmail();
    }

    // 5.2 현재 사용자의 닉네임을 가져오는 메서드 
    private String getCurrentUserNickname(int userId) {
        // userId로 현재 사용자의 닉네임을 데이터베이스에서 조회하는 로직을 구현
        UserResponseDTO user = userMapper.getUserById(userId);
        
        // 로그 추가
        if (user != null) {
            logger.info("User ID2: {}, Nickname: {}", userId, user.getNickname());
        } else {
            logger.warn("User not found for ID: {}", userId);
        }
        
        return user.getNickname();
    }

     //6. 비밀번호 변경 메소드
    @Transactional
    public ResponseEntity<String> changePw(ChangePwDTO changePwDTO) {
        logger.info("비밀번호 변경 요청: 이메일 = {}", changePwDTO.getEmail());

        // 이메일로 사용자 조회
        UserResponseDTO user = userMapper.getUserByEmail(changePwDTO.getEmail());
        
        // 사용자 존재 여부 확인
        if (user == null) {
            logger.warn("사용자를 찾을 수 없습니다: 이메일 = {}", changePwDTO.getEmail());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("{\"Code\": \"NOT_FOUND\", \"Message\": \"사용자를 찾을 수 없습니다.\"}");
        }

        logger.info("사용자 조회 성공: 이메일 = {}", user.getEmail());

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(changePwDTO.getPassword());
        logger.info("암호화된 비밀번호 생성 완료.");

        // 새 비밀번호로 설정
        user.setPassword(encodedPassword); // 비밀번호 변경
        userMapper.updateUserPassword(user); // 비밀번호 업데이트 메서드 호출

        logger.info("비밀번호 변경 완료: 이메일 = {}", user.getEmail());
        return ResponseEntity.ok("{\"Code\": \"SUCCESS\", \"Message\": \"비밀번호가 변경되었습니다.\"}");
    }

     // 5. 사용자 정보 조회 메서드
     public UserResponseDTO getUserInfo(int userId) {
        // 사용자 ID로 사용자 정보 조회
        UserResponseDTO user = userMapper.getUserById(userId);
        if (user != null) {
            logger.info("User info retrieved successfully for userId: {}", userId);
            // 이미지 URL 설정 (정적 파일 경로에 맞게 URL 생성)
            user.setMemberImg(getImageUrl(user.getMemberImg())); // 이미지 URL 설정
        } else {
            logger.warn("User not found for userId: {}", userId);
        }
        return user;
    }

    // 이미지 URL 생성 메서드
    private String getImageUrl(String memberImg) {
        return "/static/" + memberImg; // 정적 파일 경로에 맞게 URL 생성
    }


    

}

   


