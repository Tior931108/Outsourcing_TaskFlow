package com.example.outsourcing_taskflow.common.config.security;

import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
@Getter
public class JwtUtil {

    // 기본 세팅
    public static final String BEARER_PREFIX = "Bearer ";
    private static final Long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    private SecretKey key;
    private JwtParser parser;

    @Value("${jwt.secret.key}")
    private String secretKeyString;

    /**
     * 빈 초기화 메서드
     */
    @PostConstruct
    public void init() {
        byte[] bytes = Decoders.BASE64.decode(secretKeyString);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.parser = Jwts.parser()
                .verifyWith(this.key)
                .build();
    }


    /**
     * 토큰 생성
     */
    public String generateToken(Long userId, String username, UserRoleEnum role) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + TOKEN_TIME))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }


    /**
     * 토큰 유효성 검사
     */
    public boolean validateToken(String token) {
        if (token == null || token.isBlank())
            return false;

        try {
            parser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT: {}", e.toString());
            return false;
        }
    }


    /**
     * 토큰 복호화
     */
    private Claims extractAllClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public Long extractUserId(String token) {
        String subject = extractAllClaims(token).getSubject();
        return Long.parseLong(subject);
    }

}
