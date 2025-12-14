package com.example.outsourcing_taskflow.domain.user.model.response;

import lombok.Getter;

import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {

    private final String token;

}
