package com.example.outsourcing_taskflow.domain.comment.model.response;

import com.example.outsourcing_taskflow.common.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentResponse {

    private Long id;
    private Long taskId;
    private Long userId;
    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long parentId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime updatedAt;

    // Entity -> DTO , 정적 팩토리 적용
    public static UpdateCommentResponse from (Comment comment) {
        return new UpdateCommentResponse(
                comment.getId(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                comment.getContent(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
