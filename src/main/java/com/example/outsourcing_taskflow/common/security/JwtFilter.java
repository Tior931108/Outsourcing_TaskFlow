package com.example.outsourcing_taskflow.common.security;
import com.example.outsourcing_taskflow.common.security.auth.AuthUserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         * JWT 토큰 존재 여부 검사 - 토큰 없을 때니까 시큐리티에서 엔트리포인트
         */
        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.isBlank()) {
            // 토큰이 없으면 Authentication 객체를 세팅하지 않고 다음 필터로 진행
            filterChain.doFilter(request, response);
            return;
        }


        /**
         * JWT 토큰 유효성 검사 - 토큰 있을 때 예외 JwtAuthenticationEntryPoint로 넘기기
         */
        String jwt = authorization.substring(7);

        if (!jwtUtil.validateToken(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }


        /**
         * 토큰 값 복호화
         */
        Long userId = jwtUtil.extractUserId(jwt);
        String username = jwtUtil.extractUsername(jwt);
        String role = jwtUtil.extractRole(jwt);
        UserRoleEnum userRoleEnum = UserRoleEnum.valueOf(role);


        /**
         * User 생성 및 SecurityContextHolder에 Authentication 객체 생성 - 토큰 있을 때만 객체 넣기 - 과제에서 AuthUser 만들어서 생성 추천
         */
        AuthUserDto authUser = new AuthUserDto(userId, username, userRoleEnum.getRole());
        Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}