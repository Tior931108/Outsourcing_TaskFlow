package com.example.outsourcing_taskflow.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IsDeleted {
    TRUE("1", "삭제된 데이터"),
    FALSE("0", "남아있는 데이터")
    ;

    private final String isDeleted;  // 삭제 여부
    private final String description; // 설명
}
