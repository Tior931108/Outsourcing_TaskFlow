package com.example.outsourcing_taskflow.domain.dashboard.service;

import com.example.outsourcing_taskflow.common.config.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.domain.dashboard.model.response.DashboardStatsResponse;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final TaskRepository taskRepository;

    /**
     * 대시보드 통계 조회
     */
    public DashboardStatsResponse getDashboardStats(AuthUserDto authUserDto) {

        // 1. 전체 작업 수
        long totalTasks = taskRepository.countByIsDeleted(IsDeleted.FALSE);

        // 2. 완료 작업 수 (DONE)
        long completedTasks = taskRepository.countByStatusAndIsDeleted(
                TaskStatusEnum.DONE,
                IsDeleted.FALSE
        );

        // 3. 진행 중 작업 수 (IN_PROGRESS)
        long inProgressTasks = taskRepository.countByStatusAndIsDeleted(
                TaskStatusEnum.IN_PROGRESS,
                IsDeleted.FALSE
        );

        // 4. 할 일 작업 수 (TODO)
        long todoTasks = taskRepository.countByStatusAndIsDeleted(
                TaskStatusEnum.TODO,
                IsDeleted.FALSE
        );

        // 5. 기한 초과 작업 수
        long overdueTasks = taskRepository.countOverdueTasks(
                LocalDateTime.now(),
                IsDeleted.FALSE
        );

        // 6. 팀 진행률 (전체 작업 대비 완료 작업 비율)
        double teamProgress = totalTasks > 0
                ? Math.round((double) completedTasks / totalTasks * 1000.0) / 10.0
                : 0.0;

        // 7. 내 진행률 (내 작업 대비 내 완료 작업 비율)
        long myTotalTasks = taskRepository.countByAssigneeIdAndIsDeleted(
                authUserDto.getId(),
                IsDeleted.FALSE
        );
        long myCompletedTasks = taskRepository.countByAssigneeIdAndStatusAndIsDeleted(
                authUserDto.getId(),
                TaskStatusEnum.DONE,
                IsDeleted.FALSE
        );
        double completionRate = myTotalTasks > 0
                ? Math.round((double) myCompletedTasks / myTotalTasks * 1000.0) / 10.0
                : 0.0;

        return new DashboardStatsResponse(
                (int) totalTasks,
                (int) completedTasks,
                (int) inProgressTasks,
                (int) todoTasks,
                (int) overdueTasks,
                teamProgress,
                completionRate
        );
    }
}
