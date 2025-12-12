package com.example.outsourcing_taskflow.domain.member.dto.response;

import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberListResponseDto {

    // 속성
    private Long id;
    private String username;
    private String name;
    private String email;
    private UserRoleEnum role;
    private LocalDateTime createdAt;

    // 생성자
    public MemberListResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUserName();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }
}
