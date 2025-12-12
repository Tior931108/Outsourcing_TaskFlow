package com.example.outsourcing_taskflow.domain.dashboard.service;

import com.example.outsourcing_taskflow.common.config.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.domain.dashboard.model.dto.DailyTrendDto;
import com.example.outsourcing_taskflow.domain.dashboard.model.dto.TaskSummaryDto;
import com.example.outsourcing_taskflow.domain.dashboard.model.response.DashboardStatsResponse;
import com.example.outsourcing_taskflow.domain.dashboard.model.response.MyTasksResponse;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final TaskRepository taskRepository;

    // 요일 배열 (월, 화, 수, 목, 금, 토, 일)
    private static final String[] DAY_NAMES = {"월", "화", "수", "목", "금", "토", "일"};

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

    /**
     * 내 작업 요약 조회
     */
    public MyTasksResponse getMyTasks(AuthUserDto authUserDto) {

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();  // 오늘 00:00:00
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);  // 오늘 23:59:59
        LocalDateTime tomorrow = today.plusDays(1).atStartOfDay();  // 내일 00:00:00
        LocalDateTime weekLater = today.plusDays(8).atStartOfDay();  // 7일 후 00:00:00

        // 1. 오늘 마감인 작업
        List<Task> todayTasks = taskRepository.findTodayTasksByUserId(
                authUserDto.getId(),
                startOfDay,
                endOfDay,
                IsDeleted.FALSE
        );

        // 2. 다가오는 작업 (내일 ~ 7일 이내)
        List<Task> upcomingTasks = taskRepository.findUpcomingTasksByUserId(
                authUserDto.getId(),
                tomorrow,
                weekLater,
                IsDeleted.FALSE
        );

        // 3. 기한 초과 작업
        List<Task> overdueTasks = taskRepository.findOverdueTasksByUserId(
                authUserDto.getId(),
                startOfDay,
                IsDeleted.FALSE
        );

        // Entity -> DTO 변환
        List<TaskSummaryDto> todayTaskDtos = todayTasks.stream()
                .map(TaskSummaryDto::new)
                .collect(Collectors.toList());

        List<TaskSummaryDto> upcomingTaskDtos = upcomingTasks.stream()
                .map(TaskSummaryDto::new)
                .collect(Collectors.toList());

        List<TaskSummaryDto> overdueTaskDtos = overdueTasks.stream()
                .map(TaskSummaryDto::new)
                .collect(Collectors.toList());

        return new MyTasksResponse(todayTaskDtos, upcomingTaskDtos, overdueTaskDtos);
    }

    /**
     * 주간 작업 추세 조회 (최근 7일)
     */
    public List<DailyTrendDto> getWeeklyTrend() {

        List<DailyTrendDto> weeklyTrend = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 최근 7일간의 데이터 생성 (6일 전 ~ 오늘)
        for (int i = 6; i >= 0; i--) {
            LocalDate targetDate = today.minusDays(i);
            LocalDateTime startOfDay = targetDate.atStartOfDay();  // 00:00:00
            LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);  // 23:59:59.999

            // 해당 날짜에 생성된 작업 수
            long tasksCreated = taskRepository.countTasksCreatedOnDate(
                    startOfDay,
                    endOfDay,
                    IsDeleted.FALSE
            );

            // 해당 날짜에 완료된 작업 수
            long tasksCompleted = taskRepository.countTasksCompletedOnDate(
                    startOfDay,
                    endOfDay,
                    IsDeleted.FALSE
            );

            // 요일 이름 가져오기 (1=월요일, 7=일요일)
            int dayOfWeek = targetDate.getDayOfWeek().getValue();
            String dayName = DAY_NAMES[dayOfWeek - 1];

            // 날짜 형식 (YYYY-MM-DD)
            String dateString = targetDate.format(dateFormatter);

            DailyTrendDto dailyTrend = new DailyTrendDto(
                    dayName,
                    (int) tasksCreated,
                    (int) tasksCompleted,
                    dateString
            );

            weeklyTrend.add(dailyTrend);
        }

        return weeklyTrend;
    }
}
