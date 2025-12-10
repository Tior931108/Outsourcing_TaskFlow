package com.example.outsourcing_taskflow.domain.task.service;

import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import com.example.outsourcing_taskflow.domain.task.dto.request.CreateTaskRequest;
import com.example.outsourcing_taskflow.domain.task.dto.response.CreateTaskResponse;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CreateTaskResponse createTask(CreateTaskRequest request) {

        // 필수값 : 작업명, 담당자
        if (request.getTitle() == null || request.getAssigneeId() == null) {
            throw new CustomException(ErrorMessage.MUST_TITLE_TASK_USER);
        }

        // 담당자 가져오기
        User user = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_TASK_USER));

        Task createdTask = new Task(
                request.getTitle(),
                request.getDescription(),
                TaskStatusEnum.TODO,
                request.getPriority(),
                user,
                request.getDueDate()
        );

        Task savedTask = taskRepository.save(createdTask);

        return CreateTaskResponse.from(savedTask);
    }

}
