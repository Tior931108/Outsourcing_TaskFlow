package com.example.outsourcing_taskflow.domain.user.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginUserRequest {

    @NotBlank(message = "username과 password는 필수입니다.")
    private String username;

    @NotBlank(message = "username과 password는 필수입니다.")
    private String password;
}
