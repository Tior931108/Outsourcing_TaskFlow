package com.example.outsourcing_taskflow.domain.comment.service;

import com.example.outsourcing_taskflow.common.config.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.entity.Comment;
import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import com.example.outsourcing_taskflow.domain.comment.model.dto.CommentResponseDto;
import com.example.outsourcing_taskflow.domain.comment.model.request.CreateCommentRequest;
import com.example.outsourcing_taskflow.domain.comment.model.request.UpdateCommentRequest;
import com.example.outsourcing_taskflow.domain.comment.model.response.CreateCommentResponse;
import com.example.outsourcing_taskflow.domain.comment.model.response.ReadCommentResponse;
import com.example.outsourcing_taskflow.domain.comment.repository.CommentRepository;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public Page<ReadCommentResponse> getComments(Long taskId, Pageable pageable) {
        // 404 : 작업 ID 존재 여부
        if (!taskRepository.existsById(taskId)) {
            throw new CustomException(ErrorMessage.NOT_FOUND_TASK);
        }

        // DTO projection으로 직접 페이징 조회 (필요한 필드만)
        return commentRepository.findCommentDtosByTaskId(
                taskId,
                IsDeleted.FALSE,
                pageable
        );
    }

    @Transactional
    public CreateCommentResponse createComment(Long taskId, CreateCommentRequest request, AuthUserDto authUserDto) {
        // 0. User 조회
        User user = userRepository.findById(authUserDto.getId())
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_USER));

        // 1. Task 존재 여부 확인
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_TASK));

        // 2. 부모 댓글 검증 (있는 경우에만)
        Comment parent = validateAndGetParent(request.getParentId(), taskId);

        // 3. Comment 생성
        Comment comment = createCommentEntity(request.getContent(), user, task, parent);

        // 4. 저장 및 반환
        Comment savedComment = commentRepository.save(comment);

        return CreateCommentResponse.from(savedComment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long taskId, Long commentId, @Valid UpdateCommentRequest request, AuthUserDto authUserDto) {

        // 1. Task 존재 여부 확인
        if (!taskRepository.existsById(taskId)) {
            throw new CustomException(ErrorMessage.NOT_FOUND_TASK);
        }

        // 2. 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_COMMENT));

        // 3. 권한 확인: 댓글 작성자만 수정 가능
        if (!comment.getUser().getId().equals(authUserDto.getId())) {
            throw new CustomException(ErrorMessage.NOT_MODIFY_COMMENT_AUTHORIZED);
        }

        // 4. 댓글 내용 수정
        comment.updateContent(request.getContent());
        commentRepository.flush();

        // 5. 변경 감지로 자동 저장됨 (Dirty Checking)
        return CommentResponseDto.from(comment);
    }

    /**
     * 부모 댓글 검증 및 조회
     */
    private Comment validateAndGetParent(Long parentId, Long taskId) {
        if (parentId == null) {
            return null;
        }

        // 404 : 부모 댓글을 찾을 수 없습니다.
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_COMMENT));

        // 404 : 부모 댓글의 작업 ID와 추가 작성 하려는 작업 ID가 동일한지 확인 : 작업을 찾을 수 없습니다.
        if (!parent.getTask().getId().equals(taskId)) {
            throw new CustomException(ErrorMessage.NOT_FOUND_TASK);
        }

        return parent;
    }

    /**
     * Comment 엔티티 생성
     * - 부모 댓글 유무에 따라 반환 결정
     */
    private Comment createCommentEntity(String content, User user, Task task, Comment parent) {
        if (parent != null) {
            return new Comment(content, user, task, parent);
        }
        return new Comment(content, user, task);
    }
}
