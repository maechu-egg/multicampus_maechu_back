package com.multipjt.multi_pjt.user.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // Spring Security의 Authentication 임포트
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.user.domain.CustomUserDetails;
import com.multipjt.multi_pjt.user.domain.login.EmailCertificationInputDTO;
import com.multipjt.multi_pjt.user.domain.login.EmailCertificationRequestDTO;
import com.multipjt.multi_pjt.user.domain.login.EmailRequestDTO;
import com.multipjt.multi_pjt.user.domain.login.LoginDTO;
import com.multipjt.multi_pjt.user.domain.login.NicknameRequestDTO;
import com.multipjt.multi_pjt.user.domain.login.UserRequestDTO;
import com.multipjt.multi_pjt.user.service.LoginServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private LoginServiceImpl loginServiceImple;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        loginServiceImple.registerUser(userRequestDTO); // 회원가입 처리

        // 회원가입 성공 메시지를 JSON 형식으로 반환
        return ResponseEntity.ok("{\"message\": \"회원가입 성공\"}");
    }

    @PostMapping("/register/email-check")
    public ResponseEntity<String> checkEmail(@RequestBody EmailRequestDTO emailRequest) {
        String email = emailRequest.getEmail(); // DTO에서 이메일 가져오기
        boolean isValid = loginServiceImple.isValidEmail(email); // 이메일 형식 검증
        if (!isValid) { // 이메일 형식이 올바르지 않은 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("{\"Code\": \"INVALID_EMAIL\", \"Message\": \"이메일 형식이 올바르지 않습니다.\"}");
        }

        boolean exists = loginServiceImple.existsByEmail(email); // 이메일 중복 확인
        if (exists) {  // 이메일이 중복된 경우
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("{\"Code\": \"EMAIL_ALREADY_EXISTS\", \"Message\": \"이미 가입된 이메일입니다.\"}");
        } else {  // 이메일이 중복되지 않은 경우
            return ResponseEntity.ok("{\"Code\": \"SUCCESS\",\"Message\": \"사용 가능한 이메일입니다.\"}");
        }
    }

    @PostMapping("/register/nickname-check") 
    public ResponseEntity<String> checkNickname(@RequestBody NicknameRequestDTO nicknameRequestDTO) {
        String nickname = nicknameRequestDTO.getNickname();
        boolean exists = loginServiceImple.existsByNickname(nickname);
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("{\"Code\": \"NICKNAME_ALREADY_EXISTS\", \"Message\": \"이미 존재하는 닉네임입니다.\"}");
        } else {
            return ResponseEntity.ok("{\"Code\": \"SUCCESS\", \"Message\": \"사용 가능한 닉네임입니다.\"}");
        }
    }

   

    @PostMapping("/register/email-certification")
    public ResponseEntity<String> emailCertification(@RequestBody EmailCertificationRequestDTO emailDTO) {
        String email = emailDTO.getEmail();
        boolean result = loginServiceImple.sendCertificationEmail(email); // 서비스 메서드 호출

        if (result) {
            return ResponseEntity.ok("{\"Code\": \"SUCCESS\", \"Message\": \"인증 메일이 발송되었습니다.\"}");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"Code\": \"ERROR\", \"Message\": \"인증 메일 발송에 실패했습니다.\"}");
        }
    }

    
    @PostMapping("/register/verify-certification")
    public ResponseEntity<String> verifyCertification(@RequestBody EmailCertificationInputDTO emailDTO) {
        String email = emailDTO.getEmail();
        String code = emailDTO.getCertificationCode(); // 인증 코드를 가져옴

        boolean isVerified = loginServiceImple.verifyCertificationCode(email, code); // 인증 코드 확인

        if (isVerified) {
            return ResponseEntity.ok("{\"Code\": \"SUCCESS\", \"Message\": \"인증이 완료되었습니다.\"}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("{\"Code\": \"INVALID_CODE\", \"Message\": \"유효하지 않은 인증 코드입니다.\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) {
        String token = loginServiceImple.login(loginDTO); // 로그인 메서드 호출
        return ResponseEntity.ok(token); // JWT 토큰 반환
    }

    @PostMapping("/test") 
    public ResponseEntity<String> test() {
        // 현재 인증된 사용자의 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            // principal을 UserDetails로 캐스팅
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername(); // 이메일을 가져옵니다.
            return ResponseEntity.ok("인증 성공! 사용자: " + email);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
        }
    } 

    
    

    // @PostMapping("/logout")
    // public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
    //     loginServiceImple.expireToken(token); //토큰 만료 처리 
    //     return ResponseEntity.ok("logout success");
    // }
}