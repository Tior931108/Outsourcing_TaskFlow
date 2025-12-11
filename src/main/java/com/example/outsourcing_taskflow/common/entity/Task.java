package com.example.outsourcing_taskflow.common.entity;

import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.common.enums.TaskPriorityEnum;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.domain.task.dto.request.UpdateTaskRequest;
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

    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

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
    @JoinColumn(name = "user_id", nullable = false)
    private User assignee; // 담당자 - 유저 FK

    @Column(nullable = false)
    private LocalDateTime dueDate; // 마감일

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private IsDeleted isDeleted = IsDeleted.FALSE; // 삭제 여부

    // 생성자
    public Task(String title, String description, TaskStatusEnum status, TaskPriorityEnum priority, User assignee, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignee = assignee;
        this.dueDate = dueDate;
    }
    
    // 기능
    public void update(UpdateTaskRequest request, User newAssignee) {
        if (request.getTitle() != null) {
            this.title = request.getTitle();
        }
        if (request.getDescription() != null) {
            this.description = request.getDescription();
        }
        if (request.getStatus() != null) {
            this.status = request.getStatus();  // Enum 그대로
        }
        if (request.getPriority() != null) {
            this.priority = request.getPriority();  // Enum
        }

        // 담당자도 수정?
        if (newAssignee != null) {
            this.assignee = newAssignee;
        }

        if (request.getDueDate() != null) {
            this.dueDate = request.getDueDate();
        }
    }

}
