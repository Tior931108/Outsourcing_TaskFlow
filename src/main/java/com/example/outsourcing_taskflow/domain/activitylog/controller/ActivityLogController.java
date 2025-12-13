package com.example.outsourcing_taskflow.domain.activitylog.controller;

import com.example.outsourcing_taskflow.common.config.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.response.ApiResponse;
import com.example.outsourcing_taskflow.common.response.PageResponse;
import com.example.outsourcing_taskflow.domain.activitylog.model.response.GetActivityLogResponse;
import com.example.outsourcing_taskflow.domain.activitylog.model.response.GetMyActivityLogResponse;
import com.example.outsourcing_taskflow.domain.activitylog.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityLogController {
// - Properties
    private final ActivityLogService activityLogService;

// - Methods
    // - Get All
    @GetMapping
    public ResponseEntity<PageResponse<GetActivityLogResponse>> getActivityLogs(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "taskId", required = false) Long taskId,
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        // - Get Result
        Page<GetActivityLogResponse> pageResult = activityLogService.getActivityLogs(
                page, size, type, taskId, startDate, endDate);
        // - Create ResponseDto
        PageResponse<GetActivityLogResponse> body = PageResponse.success("활동 로그 조회 성공", pageResult);
        // - Return ResponseEntity<PageResponse<T>>
        return ResponseEntity.ok(body);
    }
    // - Get My
    @GetMapping("/me")
    public ResponseEntity<PageResponse<GetMyActivityLogResponse>> getMyActivityLogs(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        // - Get Result By AuthUserId
        Long authUserId = authUserDto.getId();
        Page<GetMyActivityLogResponse> pageResult = activityLogService.getMyActivityLogs(
                authUserId, page, size);
        // - Create ResponseDto
        PageResponse<GetMyActivityLogResponse> body = PageResponse.success("내 활동 로그 조회 성공", pageResult);
        // - Return ResponseEntity<PageResponse<T>>
        return ResponseEntity.ok(body);
    }
}
