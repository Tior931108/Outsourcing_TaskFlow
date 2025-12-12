package com.example.outsourcing_taskflow.domain.user.controller;

import com.example.outsourcing_taskflow.common.config.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.response.ApiResponse;
import com.example.outsourcing_taskflow.domain.user.model.request.CreateUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.request.UpdateUserRequest;
import com.example.outsourcing_taskflow.domain.user.model.request.VerifyPasswordRequest;
import com.example.outsourcing_taskflow.domain.user.model.response.CreateUserResponse;
import com.example.outsourcing_taskflow.domain.user.model.response.GetAllResponse;
import com.example.outsourcing_taskflow.domain.user.model.response.GetUserResponse;
import com.example.outsourcing_taskflow.domain.user.model.response.UpdateUserResponse;
import com.example.outsourcing_taskflow.domain.user.model.response.VerifyPasswordResponse;
import com.example.outsourcing_taskflow.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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


    /**
     * 사용자 목록 조회
     */
    @GetMapping("/api/users")
    public ResponseEntity<ApiResponse<List<GetAllResponse>>> getAll(
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        List<GetAllResponse> responseList = userService.getAll();

        ApiResponse<List<GetAllResponse>> apiResponse = new ApiResponse<>(true, "사용자 목록 조회 성공", responseList);

        ResponseEntity<ApiResponse<List<GetAllResponse>>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        return response;
    }


    /**
     * 사용자 정보 수정
     */
    @PutMapping("/api/users/{id}")
    public ResponseEntity<ApiResponse<UpdateUserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        Long authUserID = authUserDto.getId();

        UpdateUserResponse updateUser = userService.updateUser(id, request, authUserID);

        ApiResponse<UpdateUserResponse> apiResponse = new ApiResponse<>(true, "사용자 정보가 수정되었습니다.", updateUser);

        ResponseEntity<ApiResponse<UpdateUserResponse>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        return response;
    }


    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        Long authUserId = authUserDto.getId();

        userService.deleteUser(id, authUserId);

        ApiResponse<Void> apiResponse = new ApiResponse<>(true, "회원 탈퇴가 완료되었습니다.", null);

        ResponseEntity<ApiResponse<Void>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        return response;
    }


    /**
     * 비밀번호 확인
     */
    @PostMapping("/api/users/verify-password")
    public ResponseEntity<ApiResponse<VerifyPasswordResponse>> verifyPassword(
            @RequestBody VerifyPasswordRequest request,
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        Long authUserId = authUserDto.getId();

        VerifyPasswordResponse verifyPassword = userService.verifyPassword(request, authUserId);

        if (verifyPassword.isValid()) {
            ApiResponse<VerifyPasswordResponse> apiResponse = new ApiResponse<>(true, "비밀번호가 확인되었습니다.", verifyPassword);
            ResponseEntity<ApiResponse<VerifyPasswordResponse>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);
            return response;

        } else {
            ApiResponse<VerifyPasswordResponse> apiResponse = new ApiResponse<>(false, "비밀번호가 올바르지 않습니다.", verifyPassword);
            ResponseEntity<ApiResponse<VerifyPasswordResponse>> response = new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
            return response;
        }

    }
}

