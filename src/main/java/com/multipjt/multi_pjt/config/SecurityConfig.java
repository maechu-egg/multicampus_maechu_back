package com.multipjt.multi_pjt.config;

import com.multipjt.multi_pjt.jwt.JwtAuthenticationFilter; // JwtAuthenticationFilter 임포트
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // JwtAuthenticationFilter 주입

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter; // 생성자 주입
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v2/api-docs", "/swagger-ui/**", "/swagger-resources/**").permitAll() // Swagger 관련 요청 허용
                .requestMatchers("/api/user/register").permitAll() // 회원가입 API 허용
                .requestMatchers("/api/user/login").permitAll() // 로그인 API 허용
                .anyRequest().authenticated() // 나머지 요청은 인증 필요
            )
            .formLogin()  // 기본 로그인 페이지 활성화
            .and()
            .httpBasic(); // HTTP Basic 인증 활성화

        // JWT 필터 추가
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 등록

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화
    }

    // @Bean
    // public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    //     UserDetails user = User.withUsername("admin")
    //         .password(passwordEncoder.encode("admin123"))
    //         .roles("USER")
    //         .build();
    //     return new InMemoryUserDetailsManager(user);
    // }
}