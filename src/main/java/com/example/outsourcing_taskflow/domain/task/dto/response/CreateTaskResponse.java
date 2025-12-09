package com.example.outsourcing_taskflow.domain.task.dto.response;

import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.enums.TaskPriorityEnum;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.domain.team.dto.AssigneeInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateTaskResponse {

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

    public CreateTaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.assigneeId = task.getAssignee().getId();
        this.assignee = new AssigneeInfo(task.getAssignee());
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
        this.dueDate = task.getDueDate();
    }

}
