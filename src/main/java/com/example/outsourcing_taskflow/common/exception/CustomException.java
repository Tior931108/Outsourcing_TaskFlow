package com.example.outsourcing_taskflow.common.exception;

import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorMessage errorMessage;

    public CustomException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
