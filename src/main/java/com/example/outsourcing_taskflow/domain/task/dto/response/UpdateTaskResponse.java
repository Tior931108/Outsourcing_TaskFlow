package com.example.outsourcing_taskflow.domain.task.dto.response;

import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.enums.TaskPriorityEnum;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.domain.task.dto.AssigneeInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UpdateTaskResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final TaskStatusEnum status;
    private final TaskPriorityEnum priority;
    private final Long assigneeId;
    private final AssigneeInfo assignee;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
    private final LocalDateTime updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
    private final LocalDateTime dueDate;

    public static UpdateTaskResponse from(Task task) {
        return new UpdateTaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getAssignee().getId(),
                new AssigneeInfo(task.getAssignee()),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getDueDate()
        );
    }

}
