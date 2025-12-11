package com.example.outsourcing_taskflow.domain.user.model.response;

import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import com.example.outsourcing_taskflow.domain.user.model.dto.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class GetAllResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String name;
    private final UserRoleEnum role;
    private final LocalDateTime createdAt;

    public static GetAllResponse from(UserDto userDto) {
        return new GetAllResponse(
                userDto.getId(),
                userDto.getUserName(),
                userDto.getEmail(),
                userDto.getName(),
                userDto.getRole(),
                userDto.getCreatedAt()
        );
    }
}
