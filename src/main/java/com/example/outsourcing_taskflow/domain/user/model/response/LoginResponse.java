package com.example.outsourcing_taskflow.domain.user.model.response;

import lombok.Getter;

@Getter
public class LoginResponse {

    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}
