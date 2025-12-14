package com.example.outsourcing_taskflow.domain.user.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateUserRequest {

    @Pattern(
            regexp = "^[A-Za-z0-9]{4,20}$",
            message = "아이디는 4~20자의 영문 또는 숫자만 사용할 수 있습니다."
    )
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String username;    // 아이디


    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;


    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 각각 1글자 이상 포함하여 8자 이상이어야 합니다."
    )
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;


    @Size(min = 2, max = 50, message = "이름은 2~50자이어야 합니다.")
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;
}
