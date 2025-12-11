package com.example.outsourcing_taskflow.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;  // application.yml에서 관리

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // /api 하위 모든경로에 적용
                .allowedOrigins(allowedOrigins) // 프론트엔드 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")  // 또는 "Authorization", "Content-Type"
                // .allowCredentials(true)  // 쿠키/세션의 경우 필수, JWT는 헤더로 보내니까 선택사항!
                .maxAge(3600);
    }
}
