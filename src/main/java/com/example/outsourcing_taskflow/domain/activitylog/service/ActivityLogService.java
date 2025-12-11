package com.example.outsourcing_taskflow.domain.activitylog.service;

import com.example.outsourcing_taskflow.common.entity.ActivityLog;
import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ActivityType;
import com.example.outsourcing_taskflow.domain.activitylog.model.response.GetActivityLogResponse;
import com.example.outsourcing_taskflow.domain.activitylog.model.response.GetMyActivityLogResponse;
import com.example.outsourcing_taskflow.domain.activitylog.repository.ActivityLogRepository;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityLogService {
// - Properties
    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

// - Methods
    // - Log
    @Transactional
    public void log(ActivityType type,
                    User user,
                    Task task,
                    String description) {
        // - Create Log
        ActivityLog log = ActivityLog.of(type, user, task, description);
        // - Save Log
        activityLogRepository.save(log);
    }
    // - Get All ActivityLogs By Conditions
    @Transactional
    public Page<GetActivityLogResponse> getActivityLogs(
            int page, int size, String type, Long taskId, LocalDateTime startDate, LocalDateTime endDate) {
        // - Create Pageable
        Pageable pageable = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // - Set Search Conditions
        ActivityType activityType = null;
        if (type != null && !type.isBlank()) {
            activityType = ActivityType.valueOf(type);
        }

        // - Search
        Page<ActivityLog> logs = activityLogRepository.searchActivityLogs(
                activityType, taskId, startDate, endDate, pageable);

        // - Return Result
        return logs.map(GetActivityLogResponse::from);
    }
    // - Get My ActivityLogs
    public Page<GetMyActivityLogResponse> getMyActivityLogs(
            Long userId, int page, int size) {
        // - Create Pageable
        Pageable pageable = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // - Search
        Page<ActivityLog> logs =
                activityLogRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        // - Return Result
        return logs.map(GetMyActivityLogResponse::from);
    }
}
