package com.example.outsourcing_taskflow.domain.task.controller;

import com.example.outsourcing_taskflow.common.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.response.ApiResponse;
import com.example.outsourcing_taskflow.common.response.PageResponse;
import com.example.outsourcing_taskflow.domain.task.model.request.CreateTaskRequest;
import com.example.outsourcing_taskflow.domain.task.model.request.UpdateTaskRequest;
import com.example.outsourcing_taskflow.domain.task.model.request.UpdateTaskStatusRequest;
import com.example.outsourcing_taskflow.domain.task.model.response.CreateTaskResponse;
import com.example.outsourcing_taskflow.domain.task.model.response.GetTaskResponse;
import com.example.outsourcing_taskflow.domain.task.model.response.UpdateTaskResponse;
import com.example.outsourcing_taskflow.domain.task.model.response.UpdateTaskStatusResponse;
import com.example.outsourcing_taskflow.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // 작업 생성
    @PostMapping
    public ResponseEntity<ApiResponse<CreateTaskResponse>> createTask(@Valid @RequestBody CreateTaskRequest request) {
        CreateTaskResponse response = taskService.createTask(request);
//        return ApiResponse.success("작업이 생성되었습니다.", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("작업이 생성되었습니다.", response));
    }

    // 작업 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetTaskResponse>> getTask(@Valid @PathVariable Long id) {
        GetTaskResponse response = taskService.getTask(id);
//        return ApiResponse.success("작업 조회 성공", response);
        return ResponseEntity.ok(ApiResponse.success("작업 조회 성공", response));
    }

    // 작업 목록 조회
    @GetMapping
    public ResponseEntity<PageResponse<GetTaskResponse>> getAllTask(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long assigneeId) {
        Page<GetTaskResponse> result = taskService.getAllTasks(page, size, status, search, assigneeId);
//        return PageResponse.success("작업 목록 조회 성공", result);
        PageResponse<GetTaskResponse> pageResponse = PageResponse.success("작업 목록 조회 성공", result);
        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }
    
    // 작업 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateTaskResponse>> updateTask(
            @Valid @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request,
            @AuthenticationPrincipal AuthUserDto authUserDto) {
        UpdateTaskResponse response = taskService.updateTask(id, request, authUserDto);
//        return ApiResponse.success("작업이 수정되었습니다.", response);
        return ResponseEntity.ok(ApiResponse.success("작업이 수정되었습니다.", response));
    }

    // 작업 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id, @AuthenticationPrincipal AuthUserDto authUserDto) {
        taskService.deleteTask(authUserDto, id);
//        return ApiResponse.success("작업이 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("작업이 삭제되었습니다.", null));
    }

    // 작업 상태 변경 : _TODO → IN_PROGRESS → DONE 순차적 변경만 허용
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<UpdateTaskStatusResponse>> updateTaskStatus(
            @PathVariable Long id,
            @RequestBody UpdateTaskStatusRequest request,
            @AuthenticationPrincipal AuthUserDto authUserDto) {
        UpdateTaskStatusResponse response = taskService.updateTaskStatus(id, request, authUserDto);
//        return ApiResponse.success("작업 상태가 변경되었습니다.", response);
        return ResponseEntity.ok(ApiResponse.success("작업 상태가 변경되었습니다.", response));
    }

}
