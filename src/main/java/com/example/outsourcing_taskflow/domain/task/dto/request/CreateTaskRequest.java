package com.example.outsourcing_taskflow.domain.task.dto.request;

import com.example.outsourcing_taskflow.common.enums.TaskPriorityEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateTaskRequest {

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;
    @NotBlank(message = "작업 내용은 필수 입력 값입니다.")
    private String description;
    @NotNull(message = "우선순위는 필수입니다.")
    private TaskPriorityEnum priority;
    @NotNull(message = "담당자 ID는 필수입니다.")
    private Long assigneeId;
    @NotNull(message = "마감일은 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
    private LocalDateTime dueDate;

}
