package com.example.outsourcing_taskflow.domain.activitylog.repository;

import com.example.outsourcing_taskflow.common.entity.ActivityLog;
import com.example.outsourcing_taskflow.common.enums.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
// - Methods
    // - Search All By Conditions
    @Query("""
            select log
            from ActivityLog log
            where (:type is null or log.type = :type)
              and (:taskId is null or log.task.id = :taskId)
              and (:start is null or log.createdAt >= :start)
              and (:end is null or log.createdAt <= :end)
            """)
    Page<ActivityLog> searchActivityLogs(
            @Param("type") ActivityType type,
            @Param("taskId") Long taskId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);
    // - Search My ActivityLogs
    Page<ActivityLog> findByUserIdOrderByCreatedAtDesc(Long userid, Pageable pageable);
}