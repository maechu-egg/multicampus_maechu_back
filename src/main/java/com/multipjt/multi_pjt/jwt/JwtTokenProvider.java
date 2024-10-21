package com.multipjt.multi_pjt.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtTokenProvider {
    

    @Value("${secret-key}")
    private static String SECRET_KEY;
    // Access Token 만료 시간 (30분)
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
    // Refresh Token 만료 시간 (7일)
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;




    //비밀키를 사용해서 hmac sha 알고리즘으로 서명할 키 생성 
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private static final String ACCESS_TOKEN_TYPE = "access"; //access token 타입
    private static final String REFRESH_TOKEN_TYPE = "refresh"; //refresh token 타입 

    // Access Token 발급
    public static String createAccessToken(String memberId, long expireTimeMs) {
        return createToken(memberId, expireTimeMs, ACCESS_TOKEN_TYPE);
    }

    // Refresh Token 발급
    public static String createRefreshToken(String memberId, long expireTimeMs) {
        return createToken(memberId, expireTimeMs, REFRESH_TOKEN_TYPE);
    }

    // Token 발급
    private static String createToken(String memberId, long expireTimeMs, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", memberId); //사용자 id를 클레임에 추가
        claims.put("tokenType", tokenType); //토큰 타입을 클레임에 추가 

        return Jwts.builder()
                .setClaims(claims) //클레임 설정
                .setIssuedAt(new Date(System.currentTimeMillis())) //발행일자 설정
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) //토큰 말료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) //키, 알고리즘 설정 (서명)
                .compact(); //토큰 생성  
    }

    // Claims에서 memberId 꺼내기
    public static String getMemberId(String token) {
        return extractClaims(token).get("memberId", String.class);
    }

    //토큰 만료 여부 확인
    public static boolean isExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // 토큰이 Access Token인지 확인
    public static boolean isAccessToken(String token) {
        return ACCESS_TOKEN_TYPE.equals(extractClaims(token).get("tokenType", String.class));
    }

    // Refresh Token 유효성 검증
    public static boolean validateRefreshToken(String token) {
        try {
            Claims claims = extractClaims(token);
            String tokenType = claims.get("tokenType", String.class);

            if(isExpired(token)) {
                return false; //만료된 경우 false 반환
            };
            return REFRESH_TOKEN_TYPE.equals(tokenType); //만료되지 않고 리프레시 토큰일 경우 true 반환 
        } catch (TokenValidationException e) {
            return false; //예외 발생 시 false
        }
    }

     // token 파싱
    private static Claims extractClaims(String token) {
        try {
        return Jwts.parserBuilder() // jwt 파서 생성
                .setSigningKey(key) // 서명 검증을 위한 키 생성
                .build() // build() 메서드 호출
                .parseClaimsJws(token) //jwt 파싱
                .getBody(); // 클레임 반환
    } catch (ExpiredJwtException e) {
        throw new TokenValidationException("Token expired"); //만료된 토큰 예외 처ㅣㄹ
    } catch (JwtException e) {
        throw new TokenValidationException("Error in JWT processing: " + e.getMessage()); //jwt 처리 중 오류 예외 
    }
    }

    //토큰 유효성 예외 처리
    public static class TokenValidationException extends RuntimeException {
        public TokenValidationException(String message) {
            super(message);
        }
    }

}
