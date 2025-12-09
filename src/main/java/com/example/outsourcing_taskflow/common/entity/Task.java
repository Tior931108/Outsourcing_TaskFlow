package com.example.outsourcing_taskflow.common.entity;

import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.common.enums.TaskPriorityEnum;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "tasks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Builder  // 테스트 연습
//@AllArgsConstructor(access = AccessLevel.PRIVATE)  // Builder를 위해 필요
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 유저 PK

    @Column(nullable = false)
    private String title; // 작업명

    @Column(nullable = false)
    private String description; // 작업 내용

    @Column(length = 10, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TaskStatusEnum status = TaskStatusEnum.TODO; // 작업 상태, 기본값=할일

    @Column(length = 10, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TaskPriorityEnum priority; // 작업 우선순위, 기본값 없음

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User assignee; // 담당자 - 유저 FK

    @Column(nullable = false)
    private LocalDateTime dueDate; // 마감일

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private IsDeleted isDeleted = IsDeleted.FALSE; // 삭제 여부

    public Task(String title, String description, TaskStatusEnum status, TaskPriorityEnum priority, User assignee, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignee = assignee;
        this.dueDate = dueDate;
    }

}
