package com.example.outsourcing_taskflow.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskPriorityEnum {
    LOW("LOW", "낮음"),
    MEDIUM("MEDIUM", "중간"),
    HIGH("HIGH", "높음")
    ;

    private final String priority;    // 작업 우선순위
    private final String description; // 권한 설명 -> 최종적으로 필요 없을 경우, 삭제
}
