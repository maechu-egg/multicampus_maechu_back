package com.multipjt.multi_pjt.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.multipjt.multi_pjt.user.domain.CustomUserDetails;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${secret-key}")
    private String SECRET_KEY; // 인스턴스 필드로 변경

    // Access Token 만료 시간 (24시간)
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000L; // 24시간

    // 만료된 토큰을 저장할 블랙리스트
    private Set<String> blacklist = ConcurrentHashMap.newKeySet();

     // Access Token 발급
    public String createAccessToken(CustomUserDetails userDetails) {
        return createToken(userDetails, ACCESS_TOKEN_EXPIRE_TIME, "access");
    }


    // Token 발급
    private String createToken(CustomUserDetails userDetails, long expireTimeMs, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", userDetails.getMemberId()); // 사용자 id를 클레임에 추가
        claims.put("tokenType", tokenType); // 토큰 타입을 클레임에 추가 

        logger.info("Creating token with claims: {}", claims); // 클레임 로그 추가

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

    // token 파싱 : 클레임 추출 시 블랙리스트에 있는지 확인
    private Claims extractClaims(String token) {
        if (isTokenBlacklisted(token)) {
            throw new TokenValidationException("Token has been invalidated");
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenValidationException("Token expired");
        } catch (JwtException e) {
            throw new TokenValidationException("Error in JWT processing: " + e.getMessage());
        }
    }
    // 토큰 유효성 예외 처리
    public static class TokenValidationException extends RuntimeException {
        public TokenValidationException(String message) {
            super(message);
        }
    }

    // 토큰을 블랙리스트에 추가하여 만료 처리 : 토큰 무효화
    public void invalidateToken(String token) {
        blacklist.add(token);
    }

    // 토큰이 블랙리스트에 있는지 확인 :  블랙리스트에 있으면 true, 아니면 false
    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

    // 회원탈퇴를 위한 토큰에서 사용자 ID 추출
    public int getUserIdFromToken(String token) {
        Claims claims = extractClaims(token);
        logger.info("Extracted claims from token: {}", claims); // 클레임 로그 추가
        return claims.get("memberId", Integer.class); // 클레임에서 memberId를 가져옴
    }
  

    
}
