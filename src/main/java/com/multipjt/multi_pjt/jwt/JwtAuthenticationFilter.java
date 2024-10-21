package com.multipjt.multi_pjt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.multipjt.multi_pjt.user.service.MemberService;




import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor //final필드에 자동 생성자 생성
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{ //요청당 한번만 실행되는 필터
    private final MemberService memberService;

    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION); //Authorization 헤더에서 jwt 추출

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) { // Bearer 토큰 확인
            filterChain.doFilter(request, response); // 다음 필터로 요청 전달
            return;
        }

        String token = authorizationHeader.split(" ")[1]; // Bearer 이후의 토큰 추출
        System.out.println("token = " + token);

        // 토큰이 Access Token인지 확인
        try {
            if (!JwtTokenProvider.isAccessToken(token)) {
                filterChain.doFilter(request, response); // Access Token이 아닌 경우 다음 필터로 요청 전달
                return;
            }
        } catch (JwtTokenProvider.TokenValidationException e) {
            errorResponse(response); // 토큰 검증 실패 시 오류 응답
            return;
        }

        try {
            if (JwtTokenProvider.isExpired(token)) {
                throw new JwtTokenProvider.TokenValidationException("Token expired");
            }
        } catch (JwtTokenProvider.TokenValidationException e) {
            errorResponse(response); // 만료된 토큰 시 오류 응답
            return;
        }


        // Access Token인 경우 처리
        Long memberId = Long.parseLong(JwtTokenProvider.getMemberId(token)); // 사용자 ID 추출

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                memberId, null, List.of(new SimpleGrantedAuthority("USER")) // 인증 토큰 생성
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 요청 세부 정보 설정

        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // 인증 토큰 설정
        filterChain.doFilter(request, response); // 다음 필터로 요청 전달

    }

    //토큰 자체에 문제가 있을때 리턴
    private void errorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"" + "로그인 정보에 문제가 있습니다. 다시 로그인 해주세요." + "\"}");
    }
}