package com.example.outsourcing_taskflow.domain.member.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberListDto {

    // 속성
    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;
    private LocalDateTime createdAt;

    // 생성자
    public MemberListDto(Long id, String username, String name, String email, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }
}
