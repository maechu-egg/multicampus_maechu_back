package com.multipjt.multi_pjt.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.multipjt.multi_pjt.user.domain.CustomUserDetails;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

@Component
public class JwtTokenProvider {

    @Value("${secret-key}")
    private String SECRET_KEY; // 인스턴스 필드로 변경

    // Access Token 만료 시간 (24시간)
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000L; // 24시간
    // Refresh Token 만료 시간 (7일)
    //private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;

     // Access Token 발급
    public String createAccessToken(CustomUserDetails userDetails) {
        return createToken(userDetails, ACCESS_TOKEN_EXPIRE_TIME, "access");
    }

    // Refresh Token 발급 : 추후 구현 
    // public String createRefreshToken(int memberId) {
    //     return createToken(memberId, REFRESH_TOKEN_EXPIRE_TIME, "refresh");
    // }

    // Token 발급
    private String createToken(CustomUserDetails userDetails, long expireTimeMs, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", userDetails.getMemberId()); // 사용자 id를 클레임에 추가
        claims.put("tokenType", tokenType); // 토큰 타입을 클레임에 추가 

        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)); // 키 생성

        return Jwts.builder()
                .setClaims(claims) // 클레임 설정
                .setSubject(userDetails.getUsername()) // 사용자 이메일을 주제로 설정
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발행일자 설정
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) // 토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 키, 알고리즘 설정 (서명)
                .compact(); // 토큰 생성  
    }

    // CustomUserDetails에서 memberId 꺼내기
    public CustomUserDetails getUserDetailsFromToken(String token) {
        Claims claims = extractClaims(token);
        int memberId = claims.get("memberId", Integer.class);
        String email = claims.getSubject();

        return new CustomUserDetails(memberId, email, "password", new ArrayList<>());
    }

    // 토큰 만료 여부 확인
    public boolean isExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // 토큰이 Access Token인지 확인
    public boolean isAccessToken(String token) {
        return "access".equals(extractClaims(token).get("tokenType", String.class));
    }

    // token 파싱
    private Claims extractClaims(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)); // 키 생성
            return Jwts.parserBuilder() // jwt 파서 생성
                    .setSigningKey(key) // 서명 검증을 위한 키 생성
                    .build() // build() 메서드 호출
                    .parseClaimsJws(token) // jwt 파싱
                    .getBody(); // 클레임 반환
        } catch (ExpiredJwtException e) {
            throw new TokenValidationException("Token expired"); // 만료된 토큰 예외 처리
        } catch (JwtException e) {
            throw new TokenValidationException("Error in JWT processing: " + e.getMessage()); // jwt 처리 중 오류 예외 
        }
    }

    // 토큰 유효성 예외 처리
    public static class TokenValidationException extends RuntimeException {
        public TokenValidationException(String message) {
            super(message);
        }
    }
    // Refresh Token 유효성 검증
    // public boolean validateRefreshToken(String token) {
    //     try {
    //         Claims claims = extractClaims(token);
    //         String tokenType = claims.get("tokenType", String.class);

    //         if (isExpired(token)) {
    //             return false; // 만료된 경우 false 반환
    //         }
    //         return "refresh".equals(tokenType); // 만료되지 않고 리프레시 토큰일 경우 true 반환 
    //     } catch (TokenValidationException e) {
    //         return false; // 예외 발생 시 false
    //     }
    // }

    
}