package com.example.outsourcing_taskflow.domain.user.model.request;

import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class CreateUserRequest {

    private String username;
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    private String password;
    private String name;
}
