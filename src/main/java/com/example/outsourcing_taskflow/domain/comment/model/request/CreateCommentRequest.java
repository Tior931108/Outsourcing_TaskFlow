package com.example.outsourcing_taskflow.domain.comment.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCommentRequest {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(max = 200, message = "댓글 내용 255자를 넘길 수 없습니다.")
    private String content;

    private Long parentId;  // 대댓글인 경우에만 필요 (선택사항)

    // 임시: Security 적용 전 테스트용 > Security 적용 후에는 주석 처리 예정
    private Long userId;
}
