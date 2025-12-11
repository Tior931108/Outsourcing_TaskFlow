package com.example.outsourcing_taskflow.domain.activitylog.service;

import com.example.outsourcing_taskflow.common.entity.ActivityLog;
import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ActivityType;
import com.example.outsourcing_taskflow.common.enums.TaskPriorityEnum;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.domain.activitylog.repository.ActivityLogRepository;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ActivityLogServiceTest {
// - Properties
    @Mock
    private ActivityLogRepository activityLogRepository;
    @InjectMocks
    private ActivityLogService activityLogService;

// - Methods
    @Test
    void log_메서드_호출시_success_ActivityLog가_저장된다() {
        // - Given
        User user = User.builder().build();
        Task task = new Task("제목", "내용", TaskStatusEnum.TODO, TaskPriorityEnum.LOW, user, LocalDateTime.now());
        ActivityType type = ActivityType.TASK_CREATED;
        String description = "새로운 작업 \"사용자 인증 구현\"을 생성했습니다 ";

        // - When
        activityLogService.log(type, user, task, description);

        // - Then
        ArgumentCaptor<ActivityLog> captor = ArgumentCaptor.forClass(ActivityLog.class);
        verify(activityLogRepository, times(1)).save(captor.capture());

        ActivityLog savedLog = captor.getValue();
        assertThat(savedLog.getUser()).isEqualTo(user);
        assertThat(savedLog.getTask()).isEqualTo(task);
        assertThat(savedLog.getType()).isEqualTo(type);
        assertThat(savedLog.getDescription()).isEqualTo(description);
    }
}