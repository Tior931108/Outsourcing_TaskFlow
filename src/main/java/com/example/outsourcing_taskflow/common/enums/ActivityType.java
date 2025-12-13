package com.example.outsourcing_taskflow.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityType {
// - Properties
    // - ENUMS
    TASK_CREATED("작업 생성", "새로운 작업 \"%s\"을 생성했습니다."),
    TASK_UPDATED("작업 수정", "작업 \"%s\" 정보를 수정했습니다."),
    TASK_DELETED("작업 삭제", "작업 \"%s\"을 삭제했습니다."),
    TASK_STATUS_CHANGED("작업 상태 변경", "작업 상태를 \"%s\"에서 \"%s\"로 변경했습니다."),
    COMMENT_CREATED("댓글 작성", "작업 \"%s\"에 댓글을 작성했습니다."),
    COMMENT_UPDATED("댓글 수정", "댓글을 수정했습니다."),
    COMMENT_DELETED("댓글 삭제", "댓글을 삭제했습니다.");

    // - Activity Description
    private final String action;
    private final String description;
// - Methods
    // - Format Arguments
    public String format(Object... args) {
        return String.format(description, args);
    }
}
