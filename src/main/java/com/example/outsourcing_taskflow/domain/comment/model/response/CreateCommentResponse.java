package com.example.outsourcing_taskflow.domain.comment.model.response;

import com.example.outsourcing_taskflow.common.entity.Comment;
import com.example.outsourcing_taskflow.domain.user.model.dto.UserSimpleDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentResponse {

    private Long id;
    private Long taskId;
    private Long userId;
    private UserSimpleDto user;
    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)  // null이면 필드 제외
    private Long parentId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    public static CreateCommentResponse from(Comment comment) {
        return new CreateCommentResponse(
                comment.getId(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                UserSimpleDto.from(comment.getUser()),
                comment.getContent(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

}
