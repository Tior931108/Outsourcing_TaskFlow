package com.example.outsourcing_taskflow.domain.comment.model.dto;

import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import com.example.outsourcing_taskflow.domain.user.model.dto.UserDefaultDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private Long taskId;
    private Long userId;
    private UserDefaultDto user;
    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)  // null이면 필드 제외
    private Long parentId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    // JPQL Constructor Expression용 생성자 (필요한 필드만)
    public CommentDto(
            Long id,
            String content,
            Long taskId,
            Long userId,
            String username,
            String name,
            String email,
            UserRoleEnum role,
            Long parentId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.taskId = taskId;
        this.userId = userId;
        this.user = new UserDefaultDto(userId, username, name, email, role.name());
        this.parentId = parentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
