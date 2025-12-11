package com.example.outsourcing_taskflow.domain.user.controller;

import com.example.outsourcing_taskflow.common.response.ApiResponse;
import com.example.outsourcing_taskflow.domain.user.model.request.LoginUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.response.LoginResponse;
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
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginUserRequest request
    ) {
        String token = userService.login(request);

        // LoginResponse로 감싸기 ✅
        LoginResponse loginResponse = new LoginResponse(token);

        // ApiResponse로 한 번 더 감싸기 ✅
        return ResponseEntity.ok(
                ApiResponse.success("로그인 성공", loginResponse)
        );
    }
}
