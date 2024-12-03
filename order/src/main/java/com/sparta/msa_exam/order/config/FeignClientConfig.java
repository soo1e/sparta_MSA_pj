package com.sparta.msa_exam.order.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // SecurityContext에서 JWT 토큰 추출
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                // JWT 토큰을 Authorization 헤더에 추가
                String token = "Bearer " + authentication.getToken().getTokenValue();
                requestTemplate.header("Authorization", token);
            }
        };
    }
}
