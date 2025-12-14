package com.example.outsourcing_taskflow.domain.user.model.dto;

import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String userName;
    private String email;
    private String name;
    private UserRoleEnum role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
