package com.example.outsourcing_taskflow.domain.team.dto.response;

import com.example.outsourcing_taskflow.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TeamMemberResponse {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;
    private LocalDateTime createdAt;

    public TeamMemberResponse(User user) {
        this.id = user.getId();
        this.username = user.getUserName();
        this.name = user.getName();
        this.role = user.getRole().toString();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
    }
}
