package com.example.outsourcing_taskflow.domain.task.service;

import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
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
        User user = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Task createdTask = new Task(
                request.getTitle(),
                request.getDescription(),
                TaskStatusEnum.TODO,
                request.getPriority(),
                user,
                request.getDueDate()
        );

        Task savedTask = taskRepository.save(createdTask);

        return new CreateTaskResponse(savedTask);
    }

}
