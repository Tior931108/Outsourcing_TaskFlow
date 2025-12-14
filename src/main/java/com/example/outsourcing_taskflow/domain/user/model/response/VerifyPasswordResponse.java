package com.example.outsourcing_taskflow.domain.user.model.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VerifyPasswordResponse {

    private boolean valid;

    public void setValid(boolean valid) {
        this.valid = valid;
    }

}
