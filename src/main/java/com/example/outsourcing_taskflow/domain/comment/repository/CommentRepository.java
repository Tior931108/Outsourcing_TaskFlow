package com.example.outsourcing_taskflow.domain.comment.repository;

import com.example.outsourcing_taskflow.common.entity.Comment;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.domain.comment.model.dto.CommentResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    /**
     * 특정 작업의 모든 댓글 조회 (부모 + 자식 모두)
     * 페이징은 애플리케이션 레벨에서 처리
     * DTO 프로젝션으로 원하는 컬럼만 추출 + (N+1)문제 해결
     */
    @Query("SELECT new com.example.outsourcing_taskflow.domain.comment.model.dto.CommentResponseDto(" +
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
            "WHERE c.task.id = :taskId " +
            "AND c.isDeleted = :isDeleted")
    List<CommentResponseDto> findAllCommentsByTaskId(
            @Param("taskId") Long taskId,
            @Param("isDeleted") IsDeleted isDeleted);


    /**
     * 부모 댓글의 모든 자식 댓글을 한 번에 Soft Delete
     */
    @Modifying
    @Query("UPDATE Comment c SET c.isDeleted = 'TRUE' WHERE c.parent.id = :parentId AND c.isDeleted = 'FALSE'")
    void softDeleteRepliesByParentId(@Param("parentId") Long parentId);


}
