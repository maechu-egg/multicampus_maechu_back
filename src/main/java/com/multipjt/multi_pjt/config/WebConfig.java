package com.multipjt.multi_pjt.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
            .allowedOrigins("http://localhost:3000") // 프론트엔드 URL 지정
            .allowedMethods("*") // 모든 HTTP 메서드 허용
            .allowedHeaders("*") // 모든 헤더 허용
            .allowCredentials(true); // 쿠키 및 인증 정보 허용
            
    }
//정적 파일 경로 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 리소스 경로 설정
        registry.addResourceHandler("/static/**") // "/static/**" 경로로 들어오는 요청을 처리
                .addResourceLocations("classpath:/static/"); // "src/main/resources/static/" 경로에서 리소스를 찾음
    }
}
