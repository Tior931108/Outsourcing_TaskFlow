package com.example.outsourcing_taskflow.domain.dashboard.model.response;

import com.example.outsourcing_taskflow.domain.dashboard.model.dto.TaskSummaryDto;
import lombok.Getter;

import java.util.List;

@Getter
public class MyTasksResponse {

    private List<TaskSummaryDto> todayTasks;      // 오늘 마감
    private List<TaskSummaryDto> upcomingTasks;   // 다가오는 작업
    private List<TaskSummaryDto> overdueTasks;    // 기한 초과

    public MyTasksResponse(List<TaskSummaryDto> todayTasks,
                           List<TaskSummaryDto> upcomingTasks,
                           List<TaskSummaryDto> overdueTasks) {
        this.todayTasks = todayTasks;
        this.upcomingTasks = upcomingTasks;
        this.overdueTasks = overdueTasks;
    }
}
