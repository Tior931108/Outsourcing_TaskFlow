package com.example.outsourcing_taskflow.common.entity;

import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    // 자기 참조: 부모 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    //댓글 내용
    @Column(length = 200, nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private IsDeleted isDeleted = IsDeleted.FALSE; // 삭제 여부

    // 통합 생성자 (parent는 선택적)
    public Comment(String content, User user, Task task, Comment parent) {
        this.content = content;
        this.user = user;
        this.task = task;
        this.parent = parent;  // null 가능
        this.isDeleted = IsDeleted.FALSE;
    }

    // 일반 댓글용 편의 생성자
    public Comment(String content, User user, Task task) {
        this(content, user, task, null);
    }

    // 댓글 내용 수정
    public void updateContent(String content) {
        this.content = content;
    }

    // 댓글 삭제 (Soft Delete)
    public void delete() {
        this.isDeleted = IsDeleted.TRUE;
    }
}
