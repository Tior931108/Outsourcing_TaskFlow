package com.example.outsourcing_taskflow.domain.activitylog.repository;

import com.example.outsourcing_taskflow.common.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
// - Find Methods
}