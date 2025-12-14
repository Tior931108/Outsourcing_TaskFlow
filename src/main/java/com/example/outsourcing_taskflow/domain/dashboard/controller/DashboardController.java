package com.example.outsourcing_taskflow.domain.dashboard.controller;

import com.example.outsourcing_taskflow.common.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.response.ApiResponse;
import com.example.outsourcing_taskflow.domain.dashboard.model.dto.DailyTrendDto;
import com.example.outsourcing_taskflow.domain.dashboard.model.response.DashboardStatsResponse;
import com.example.outsourcing_taskflow.domain.dashboard.model.response.MyTasksResponse;
import com.example.outsourcing_taskflow.domain.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 대시보드 통계 조회
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats(
            @AuthenticationPrincipal AuthUserDto authUserDto) {

        DashboardStatsResponse stats = dashboardService.getDashboardStats(authUserDto);

        return ResponseEntity.ok(
                ApiResponse.success("대시보드 통계 조회 성공", stats)
        );
    }

    /**
     * 내 작업 요약 조회
     */
    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<MyTasksResponse>> getMyTasks(
            @AuthenticationPrincipal AuthUserDto authUserDto) {

        MyTasksResponse myTasks = dashboardService.getMyTasks(authUserDto);

        return ResponseEntity.ok(
                ApiResponse.success("내 작업 요약 조회 성공", myTasks)
        );
    }

    /**
     * 주간 작업 추세 조회
     */
    @GetMapping("/weekly-trend")
    public ResponseEntity<ApiResponse<List<DailyTrendDto>>> getWeeklyTrend() {

        List<DailyTrendDto> weeklyTrend = dashboardService.getWeeklyTrend();

        return ResponseEntity.ok(
                ApiResponse.success("주간 작업 추세 조회 성공", weeklyTrend)
        );
    }
}
