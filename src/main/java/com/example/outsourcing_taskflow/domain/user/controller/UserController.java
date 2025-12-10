package com.example.outsourcing_taskflow.domain.user.controller;

import com.example.outsourcing_taskflow.common.response.ApiResponse;
import com.example.outsourcing_taskflow.domain.user.model.request.CreateUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.response.CreateUserResponse;
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
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/api/users")
    public ResponseEntity<ApiResponse<CreateUserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        CreateUserResponse createUser = userService.createUser(request);

        ApiResponse<CreateUserResponse> apiResponse = new ApiResponse<>(true, "회원가입이 완료되었습니다.", createUser);

        ResponseEntity<ApiResponse<CreateUserResponse>> response = new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

        return response;
    }
}
