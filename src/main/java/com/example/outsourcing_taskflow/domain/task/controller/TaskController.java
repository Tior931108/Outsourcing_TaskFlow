package com.example.outsourcing_taskflow.domain.task.controller;

import com.example.outsourcing_taskflow.common.response.ApiResponse;
import com.example.outsourcing_taskflow.domain.task.dto.request.CreateTaskRequest;
import com.example.outsourcing_taskflow.domain.task.dto.response.CreateTaskResponse;
import com.example.outsourcing_taskflow.domain.task.dto.response.GetTaskResponse;
import com.example.outsourcing_taskflow.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // 작업 생성
    @PostMapping
    public ApiResponse<CreateTaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        CreateTaskResponse response = taskService.createTask(request);
        return ApiResponse.success("작업이 생성되었습니다.", response);
    }

    // 작성 상세 조회
    @GetMapping("/{id}")
    public ApiResponse<GetTaskResponse> getTask(@Valid @PathVariable Long id) {
        GetTaskResponse response = taskService.getTask(id);
        return ApiResponse.success("작업 조회 성공", response);
    }

}
