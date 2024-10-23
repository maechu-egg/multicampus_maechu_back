package com.multipjt.multi_pjt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.multipjt.multi_pjt.user.domain.CustomUserDetails;

import java.io.IOException;

@RequiredArgsConstructor // final 필드에 자동 생성자 생성
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 요청당 한번만 실행되는 필터
    private final JwtTokenProvider jwtTokenProvider; // JwtTokenProvider 인스턴스 주입

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // Authorization 헤더에서 jwt 추출

        // Swagger 관련 요청에 대한 인증 건너뛰기
        if (request.getRequestURI().startsWith("/v2/api-docs") || 
            request.getRequestURI().startsWith("/swagger-ui/") || 
            request.getRequestURI().startsWith("/swagger-resources/")) {
            filterChain.doFilter(request, response); // 다음 필터로 요청 전달
            return; // 필터 체인 진행 중단
        }

        // /register 요청에 대한 인증 건너뛰기
        if (request.getRequestURI().equals("/api/user/register")) {
            filterChain.doFilter(request, response); // 다음 필터로 요청 전달
            return; // 필터 체인 진행 중단
        }

        // /login 요청에 대한 인증 건너뛰기
        if (request.getRequestURI().equals("/api/user/login")) {
            filterChain.doFilter(request, response); // 다음 필터로 요청 전달
            return; // 필터 체인 진행 중단
        }

        // Authorization 헤더가 없거나 Bearer로 시작하지 않는 경우
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            errorResponse(response); // 오류 응답 호출
            return; // 필터 체인 진행 중단
        }

        String token = authorizationHeader.split(" ")[1]; // Bearer 이후의 토큰 추출
        System.out.println("token = " + token);

        // 토큰이 Access Token인지 확인
        try {
            if (!jwtTokenProvider.isAccessToken(token)) { // JwtTokenProvider 인스턴스를 사용
                errorResponse(response); // Access Token이 아닌 경우 오류 응답 호출
                return; // 필터 체인 진행 중단
            }
        } catch (JwtTokenProvider.TokenValidationException e) {
            errorResponse(response); // 토큰 검증 실패 시 오류 응답
            return;
        }

        try {
            if (jwtTokenProvider.isExpired(token)) { // JwtTokenProvider 인스턴스를 사용
                throw new JwtTokenProvider.TokenValidationException("Token expired");
            }
        } catch (JwtTokenProvider.TokenValidationException e) {
            errorResponse(response); // 만료된 토큰 시 오류 응답
            return;
        }

        // Access Token인 경우 처리
        CustomUserDetails userDetails = jwtTokenProvider.getUserDetailsFromToken(token); // CustomUserDetails 추출

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities() // 인증 토큰 생성
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 요청 세부 정보 설정

        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 인증 토큰 설정
        filterChain.doFilter(request, response); // 다음 필터로 요청 전달
    }

    // 토큰 자체에 문제가 있을 때 리턴
    private void errorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"" + "인증 실패" + "\"}");
    }
}
