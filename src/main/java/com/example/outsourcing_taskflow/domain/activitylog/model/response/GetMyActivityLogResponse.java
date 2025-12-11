package com.example.outsourcing_taskflow.domain.activitylog.model.response;

import com.example.outsourcing_taskflow.common.entity.ActivityLog;
import com.example.outsourcing_taskflow.common.enums.ActivityType;
import com.example.outsourcing_taskflow.domain.activitylog.model.dto.ActivityUserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class GetMyActivityLogResponse {
// - Properties
    private final Long id;
    private final Long userId;
    private final ActivityUserDto user;
    private final String action;
    private final String targetType;
    private final Long targetId;
    private final String description;
    private final LocalDateTime createdAt;

// - Methods
    // - Static Factory Method
    public static GetMyActivityLogResponse from(ActivityLog log) {
        ActivityType type = log.getType();
        return new GetMyActivityLogResponse(
                log.getId(),
                log.getUser().getId(),
                new ActivityUserDto(
                        log.getUser().getId(),
                        log.getUser().getUserName(),
                        log.getUser().getName()),
                type.getAction(),
                type.name(),
                log.getTask().getId(),
                log.getDescription(),
                log.getCreatedAt());
    }
}
