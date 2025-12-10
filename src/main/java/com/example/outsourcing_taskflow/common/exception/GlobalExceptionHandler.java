package com.example.outsourcing_taskflow.common.exception;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    // Validation 실패 (400 Bad request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        // 에러 응답 Map 저장
        Map<String, Object> errorResponse = new HashMap<>();
        // validaion 유효성 검사 메시지 저장
        Map<String, String> errors = new HashMap<>();

        // 모든 필드 에러 수집
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // 상태 코드 표시
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        // validation 유효성 검사 내용
        errorResponse.put("errors", errors);


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 커스텀 예외 처리
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException e) {
        log.error("CustomException 발생 {} : ", e.getMessage());

        // 에러 응답 Map 저장
        Map<String, Object> errorResponse = new HashMap<>();
        // 커스텀 비즈니스 유효성 검사 메시지 저장
        Map<String, String> errors = new HashMap<>();

        // 상태 코드 및 Error Enum 표시
        errorResponse.put("code", e.getErrorMessage());
        errorResponse.put("status", e.getErrorMessage().getStatus().value());
        // 커스텀 유효성 검사 내용
        errorResponse.put("errors", e.getMessage());

        return ResponseEntity.status(e.getErrorMessage().getStatus()).body(errorResponse);
    }
}
