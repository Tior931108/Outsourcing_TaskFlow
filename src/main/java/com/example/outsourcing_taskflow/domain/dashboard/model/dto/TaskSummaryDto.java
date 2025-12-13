package com.example.outsourcing_taskflow.domain.dashboard.model.dto;

import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.enums.TaskPriorityEnum;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.domain.user.model.dto.UserSimpleDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskSummaryDto {

    private Long id;
    private String title;
    private String status;
    private String priority;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
    private LocalDateTime dueDate;

    private UserSimpleDto assignee;  // 명세서 변경으로 추가

    public TaskSummaryDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.status = task.getStatus().name();
        this.priority = task.getPriority().name();
        this.dueDate = task.getDueDate();
        this.assignee = UserSimpleDto.from(task.getAssignee());
    }

    // JPQL Constructor Expression용
    public TaskSummaryDto(Long id, String title, TaskStatusEnum status,
                          TaskPriorityEnum priority, LocalDateTime dueDate) {
        this.id = id;
        this.title = title;
        this.status = status.name();
        this.priority = priority.name();
        this.dueDate = dueDate;
    }
}
