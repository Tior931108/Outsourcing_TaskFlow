package com.example.outsourcing_taskflow.common.entity;

import com.example.outsourcing_taskflow.common.enums.ActivityType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "activitylogs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ActivityLog {
// - Properties
    // - 고유ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // - 활동 타입
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, length = 255)
    private ActivityType type;
    // - 사용자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    // - 작업 PK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id")
    private Task task;
    // - 로그 내용
    @Column(name = "description", nullable = false, length = 255)
    private String description;
    // - 생성일
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

// - Method
    // - Factory Method
    public static ActivityLog of(ActivityType type,
                                 User user,
                                 Task task,
                                 String description) {
        return new ActivityLog(null, type, user, task, description, null);
    }
}