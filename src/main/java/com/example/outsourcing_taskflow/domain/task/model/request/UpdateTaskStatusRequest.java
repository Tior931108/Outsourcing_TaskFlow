package com.example.outsourcing_taskflow.domain.task.model.request;

import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateTaskStatusRequest {

    @NotNull(message = "작업상태는 필수입니다.")
    private TaskStatusEnum status;

}
