package com.example.outsourcing_taskflow.domain.comment.repository;

import com.example.outsourcing_taskflow.common.entity.Comment;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.domain.comment.model.response.ReadCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 필요한 필드만 선택하여 DTO projection으로 직접 조회
     * N + 1 문제 해결
     */
    @Query("SELECT new com.example.outsourcing_taskflow.domain.comment.model.response.ReadCommentResponse(" +
            "c.id, " +
            "c.content, " +
            "c.task.id, " +
            "c.user.id, " +
            "c.user.userName, " +
            "c.user.name, " +
            "c.user.email, " +
            "c.user.role, " +
            "c.parent.id, " +
            "c.createdAt, " +
            "c.updatedAt) " +
            "FROM Comment c " +
            "JOIN c.user " +
            "JOIN c.task " +
            "LEFT JOIN c.parent " +
            "WHERE c.task.id = :taskId " +
            "AND c.isDeleted = :isDeleted")
    Page<ReadCommentResponse> findCommentDtosByTaskId(
            @Param("taskId") Long taskId,
            @Param("isDeleted") IsDeleted isDeleted,
            Pageable pageable);
}
