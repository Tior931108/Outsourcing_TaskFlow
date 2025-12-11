package com.example.outsourcing_taskflow.common.filter;
import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import com.example.outsourcing_taskflow.common.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         * 로그인, 회원가입 인증 제외 - 화이트리스트 구현하기
         */
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/api/auth/login") || (requestURI.equals("/api/users") && request.getMethod().equals("POST"))) {
            filterChain.doFilter(request, response);
            return;
        }


        /**
         * JWT 토큰 존재 여부 검사
         */
        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.isBlank()) {
            log.info("JWT 토큰이 필요합니다.");
            throw new CustomException(ErrorMessage.NEED_TO_VALID_TOKEN);

//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요합니다.");
//            return;
        }


        /**
         * JWT 토큰 유효성 검사
         */
        String jwt = authorization.substring(7);

        if (!jwtUtil.validateToken(jwt)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
        }


        /**
         * 토큰 값 복호화
         */
        Long userId = jwtUtil.extractUserId(jwt);
        String username = jwtUtil.extractUsername(jwt);
        String role = jwtUtil.extractRole(jwt);
        UserRoleEnum userRoleEnum = UserRoleEnum.valueOf(role);


        /**
         * User 생성 및 SecurityContextHolder에 Authentication 객체 생성
         */

        // CustomUserDetails 커스텀 해보기
        // 태호님 뉴스피드 참고하기
        // UsernamePasswordAuthenticationToken 바로 넣지 말고, Authentication 객체로 받아서 넣어주기  -> SecurityContextHolder에

        User user = new User(username, "", List.of(userRoleEnum::getRole));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        filterChain.doFilter(request, response);
    }
}