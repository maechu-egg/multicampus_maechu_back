package com.multipjt.multi_pjt.user.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.multipjt.multi_pjt.jwt.JwtTokenProvider;
import com.multipjt.multi_pjt.user.domain.mypage.ProfileRequestDTO;
import com.multipjt.multi_pjt.user.domain.mypage.ProfileResponseDTO;
import com.multipjt.multi_pjt.user.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("profile")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private ProfileService profileService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<String> postMethodName(@RequestBody ProfileRequestDTO profileRequestDTO, 
                            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
            
            // 로그 출력
            logger.info("Extracted member_id from token: {}", userId);

            // 프로필 등록 및 결과 반환
            return profileService.registerProfile(profileRequestDTO, userId);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody ProfileRequestDTO profileRequestDTO, 
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
            
            // 로그 출력
            logger.info("Extracted member_id from token: {}", userId);

            // 사용자 정보 수정 처리
            ResponseEntity<String> response = profileService.updateProfile(userId, profileRequestDTO); // 서비스 메서드 호출

            return response; // 서비스에서 반환된 응답을 그대로 반환
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }
    @GetMapping("/")
    public ResponseEntity<ProfileResponseDTO> getProfile( @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 접두사 제거
            int userId = jwtTokenProvider.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
            
            // 로그 출력
            logger.info("Extracted member_id from token 프로필 조회시: {}", userId);
             // 사용자 정보 수정 처리
            ProfileResponseDTO response = profileService.getProfile(userId); // 서비스 메서드 호출
            if (response != null) {
                return ResponseEntity.ok(response); // 사용자 정보 반환
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(null); // 사용자 정보가 없을 경우 404 반환
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 인증 실패
        }
    }
    
}
