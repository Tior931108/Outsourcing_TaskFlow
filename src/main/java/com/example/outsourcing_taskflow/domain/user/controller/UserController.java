package com.example.outsourcing_taskflow.domain.user.controller;

import com.example.outsourcing_taskflow.common.config.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.response.ApiResponse;
import com.example.outsourcing_taskflow.domain.user.model.request.CreateUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.response.CreateUserResponse;
import com.example.outsourcing_taskflow.domain.user.model.response.GetUserResponse;
import com.example.outsourcing_taskflow.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


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

    /**
     * 사용자 정보 조회
     */
    @GetMapping("/api/users/{id}")
    public ResponseEntity<ApiResponse<GetUserResponse>> getUser(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        Long authUserId = authUserDto.getId();

        GetUserResponse getUser = userService.getUser(id, authUserId);

        ApiResponse<GetUserResponse> apiResponse = new ApiResponse<>(true, "사용자 정보 조회 성공", getUser);

        ResponseEntity<ApiResponse<GetUserResponse>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        return response;
    }
}
