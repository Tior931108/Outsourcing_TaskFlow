package com.example.outsourcing_taskflow.domain.user.model.dto;

import com.example.outsourcing_taskflow.common.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleDto {

    private Long id;
    private String username;
    private String name;

    public static UserSimpleDto from(User user) {
        return new UserSimpleDto(
            user.getId(),
            user.getUserName(),
            user.getName()
        );
    }
}
