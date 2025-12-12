package com.example.outsourcing_taskflow.domain.user.model.response;

import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import com.example.outsourcing_taskflow.domain.user.model.dto.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UpdateUserResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String name;
    private final UserRoleEnum role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UpdateUserResponse from(UserDto userDto) {
        return new UpdateUserResponse(
                userDto.getId(),
                userDto.getUserName(),
                userDto.getEmail(),
                userDto.getName(),
                userDto.getRole(),
                userDto.getCreatedAt(),
                userDto.getUpdatedAt()
        );
    }
}
