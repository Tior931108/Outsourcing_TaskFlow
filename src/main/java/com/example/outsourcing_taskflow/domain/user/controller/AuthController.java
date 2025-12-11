package com.example.outsourcing_taskflow.domain.user.controller;

import com.example.outsourcing_taskflow.common.response.ApiResponse;
import com.example.outsourcing_taskflow.domain.user.model.request.LoginUserRequest;
import com.example.outsourcing_taskflow.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponse<String>> login(
            @Valid @RequestBody LoginUserRequest request
    ) {
        String token = userService.login(request);

        ApiResponse<String> apiResponse = new ApiResponse<>(true, "로그인 성공", token);

        ResponseEntity<ApiResponse<String>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        return response;
    }
}
