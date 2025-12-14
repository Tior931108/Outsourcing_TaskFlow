package com.example.outsourcing_taskflow.domain.user.model.dto;

import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDefaultDto {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String role;

    // JPQL용 생성자 (필요한 필드만)
    public UserDefaultDto(Long id, String username, String name, String email, UserRoleEnum role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role.name();
    }
}
