package com.example.outsourcing_taskflow.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatusEnum {
    TODO("TODO", "할 일"),
    IN_PROGRESS("IN_PROGRESS", "진행 중"),
    DONE("DONE", "완료"),
    ;

    private final String status;        // 작업 상태
    private final String description;   // 상태 설명 -> 최종적으로 필요 없을 경우, 삭제
}
