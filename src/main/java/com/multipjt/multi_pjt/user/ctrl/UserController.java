 package com.multipjt.multi_pjt.user.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // Spring Security의 Authentication 임포트
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multipjt.multi_pjt.user.domain.CustomUserDetails;
import com.multipjt.multi_pjt.user.domain.LoginDTO;
import com.multipjt.multi_pjt.user.domain.UserRequestDTO;
import com.multipjt.multi_pjt.user.service.LoginServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private LoginServiceImpl loginServiceImple;

    @PostMapping("/register")
public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
    loginServiceImple.registerUser(userRequestDTO); // 회원가입 처리

    // 회원가입 성공 메시지를 JSON 형식으로 반환
    return ResponseEntity.ok("{\"message\": \"회원가입 성공\"}");
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
}

