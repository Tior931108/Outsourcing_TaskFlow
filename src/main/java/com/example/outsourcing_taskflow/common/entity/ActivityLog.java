package com.example.outsourcing_taskflow.common.entity;

import com.example.outsourcing_taskflow.common.enums.ActivityType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "activitylogs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ActivityLog {
// - Properties
    // - 고유ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // - 활동 타입
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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
    @Column(nullable = false, length = 255)
    private String description;
    // - 생성일
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
}