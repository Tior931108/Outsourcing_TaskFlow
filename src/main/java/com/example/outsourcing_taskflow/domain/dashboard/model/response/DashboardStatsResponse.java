package com.example.outsourcing_taskflow.domain.dashboard.model.response;

import lombok.Getter;

@Getter
public class DashboardStatsResponse {

    private Integer totalTasks;           // 전체 작업
    private Integer completedTasks;       // 완료 작업 (DONE)
    private Integer inProgressTasks;      // 진행 중 작업 (IN_PROGRESS)
    private Integer todoTasks;            // 할 일 작업 (TODO)
    private Integer overdueTasks;         // 기한 초과 작업
    private Double teamProgress;          // 팀 진행률
    private Double completionRate;        // 내 진행률

    public DashboardStatsResponse(
            Integer totalTasks,
            Integer completedTasks,
            Integer inProgressTasks,
            Integer todoTasks,
            Integer overdueTasks,
            Double teamProgress,
            Double completionRate) {
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.inProgressTasks = inProgressTasks;
        this.todoTasks = todoTasks;
        this.overdueTasks = overdueTasks;
        this.teamProgress = teamProgress;
        this.completionRate = completionRate;
    }
}
