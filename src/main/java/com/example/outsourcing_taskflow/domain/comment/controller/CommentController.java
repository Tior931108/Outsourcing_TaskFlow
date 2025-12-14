package com.example.outsourcing_taskflow.domain.comment.controller;

import com.example.outsourcing_taskflow.common.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.response.ApiResponse;
import com.example.outsourcing_taskflow.common.response.PageResponse;
import com.example.outsourcing_taskflow.domain.comment.model.response.UpdateCommentResponse;
import com.example.outsourcing_taskflow.domain.comment.model.request.CreateCommentRequest;
import com.example.outsourcing_taskflow.domain.comment.model.request.UpdateCommentRequest;
import com.example.outsourcing_taskflow.domain.comment.model.response.CreateCommentResponse;
import com.example.outsourcing_taskflow.domain.comment.model.dto.CommentDto;
import com.example.outsourcing_taskflow.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 작업 댓글 조회 + 페이징
     * @param taskId 작업 아이디
     * @param page 페이지 수
     * @param size 한 페이지 당 갯수
     * @param sort 정렬
     * @return 댓글 조회 페이징 반환
     */
    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<PageResponse<CommentDto>> getComments(
            @PathVariable Long taskId,
            @RequestParam(required = false , defaultValue = "0") int page,
            @RequestParam(required = false , defaultValue = "10") int size,
            @RequestParam(required = false , defaultValue = "newest") String sort) {

        // 정렬 방향 결정
        Sort.Direction direction = sort.equals("newest")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        // Pageable 생성 + 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        // 댓글 조회
        Page<CommentDto> commentsPage = commentService.getComments(taskId, pageable);

        // PageResponse 생성
        PageResponse<CommentDto> pageResponse = PageResponse.success(
                "댓글 목록을 조회했습니다.",
                commentsPage
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pageResponse);
    }

    /**
     * 댓글 및 대댓글 생성
     * @param taskId 작업 아이디
     * @param request 댓글 내용 + 부모댓글(선택적)
     * @return 댓글 생성
     */
    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<ApiResponse<CreateCommentResponse>> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal AuthUserDto authUserDto) {

        CreateCommentResponse response = commentService.createComment(taskId, request, authUserDto); // security 적용 전 userId 임시 적용

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("댓글이 작성되었습니다.", response));
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<UpdateCommentResponse>> updateComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal AuthUserDto authUserDto) {

        UpdateCommentResponse responseDto = commentService.updateComment(
                taskId,
                commentId,
                request,
                authUserDto
        );

        return ResponseEntity.ok(
                ApiResponse.success("댓글이 수정되었습니다.", responseDto)
        );
    }

    /**
     * 댓글 삭제 (Soft Delete)
     */
    @DeleteMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal AuthUserDto authUserDto) {

        commentService.deleteComment(taskId, commentId, authUserDto);

        return ResponseEntity.ok(
                ApiResponse.success("댓글이 삭제되었습니다.", null)
        );
    }

}
