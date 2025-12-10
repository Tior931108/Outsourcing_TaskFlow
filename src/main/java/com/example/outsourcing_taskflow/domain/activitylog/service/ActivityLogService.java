package com.example.outsourcing_taskflow.domain.activitylog.service;

import com.example.outsourcing_taskflow.common.entity.ActivityLog;
import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ActivityType;
import com.example.outsourcing_taskflow.domain.activitylog.repository.ActivityLogRepository;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivityLogService {
// - Properties
    private final ActivityLogRepository activitylogRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

// - Methods
    // - Log
    @Transactional
    public void log(ActivityType type,
                    Long userId,
                    Long taskId,
                    String description) {
        // - Find User, Task
        User user = userRepository.getReferenceById(userId);
        Task task = taskRepository.getReferenceById(taskId);

        // - Create Log
        ActivityLog log = ActivityLog.of(type, user, task, description);

        // - Save Log
        activitylogRepository.save(log);
    }
    // - If exist User, Task Object
    @Transactional
    public void log(ActivityType type,
                    User user,
                    Task task,
                    String description) {
        // - Create Log
        ActivityLog log = ActivityLog.of(type, user, task, description);

        // - Save Log
        activitylogRepository.save(log);
    }
}
