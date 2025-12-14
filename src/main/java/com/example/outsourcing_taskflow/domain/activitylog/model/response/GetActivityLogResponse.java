package com.example.outsourcing_taskflow.domain.activitylog.model.response;

import com.example.outsourcing_taskflow.common.entity.ActivityLog;
import com.example.outsourcing_taskflow.domain.activitylog.model.dto.ActivityUserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class GetActivityLogResponse {
// - Properties
    private final Long id;
    private final String type;
    private final Long userId;
    private final ActivityUserDto user;
    private final Long taskId;
    private final LocalDateTime timestamp;
    private final String description;

// - Methods
    // - Static Factory Method
    public static GetActivityLogResponse from(ActivityLog log) {
        return new GetActivityLogResponse(
                log.getId(),
                log.getType().name(),
                log.getUser().getId(),
                new ActivityUserDto(
                        log.getUser().getId(),
                        log.getUser().getUserName(),
                        log.getUser().getName()),
                log.getTask().getId(),
                log.getCreatedAt(),
                log.getDescription()
        );
    }
}
