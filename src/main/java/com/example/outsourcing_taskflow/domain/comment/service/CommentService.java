package com.example.outsourcing_taskflow.domain.comment.service;

import com.example.outsourcing_taskflow.common.annotaion.MeasureAllMethods;
import com.example.outsourcing_taskflow.common.config.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.entity.Comment;
import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ActivityType;
import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import com.example.outsourcing_taskflow.domain.activitylog.service.ActivityLogService;
import com.example.outsourcing_taskflow.domain.comment.model.response.UpdateCommentResponse;
import com.example.outsourcing_taskflow.domain.comment.model.request.CreateCommentRequest;
import com.example.outsourcing_taskflow.domain.comment.model.request.UpdateCommentRequest;
import com.example.outsourcing_taskflow.domain.comment.model.response.CreateCommentResponse;
import com.example.outsourcing_taskflow.domain.comment.model.dto.CommentResponseDto;
import com.example.outsourcing_taskflow.domain.comment.repository.CommentRepository;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
@MeasureAllMethods
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getComments(Long taskId, Pageable pageable) {
        // 404 : 작업 ID 존재 여부
        if (!taskRepository.existsById(taskId)) {
            throw new CustomException(ErrorMessage.NOT_FOUND_TASK);
        }

        // 1. 모든 댓글 조회 (부모 + 자식)
        List<CommentResponseDto> allComments = commentRepository.findAllCommentsByTaskId(
                taskId,
                IsDeleted.FALSE
        );

        // 2. 댓글이 없으면 빈 페이지 반환
        if (allComments.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // 3. 부모 댓글과 대댓글 분리
        List<CommentResponseDto> parentComments = new ArrayList<>();
        Map<Long, List<CommentResponseDto>> repliesByParentId = new HashMap<>();

        for (CommentResponseDto comment : allComments) {
            if (comment.getParentId() == null) {
                // 부모 댓글
                parentComments.add(comment);
            } else {
                // 대댓글 - 부모 ID로 그룹화
                repliesByParentId
                        .computeIfAbsent(comment.getParentId(), k -> new ArrayList<>())
                        .add(comment);
            }
        }

        // 4. 부모 댓글 정렬 (메모리에서 정렬 처리)
        Sort.Order order = pageable.getSort().iterator().next();
        Comparator<CommentResponseDto> comparator = order.getDirection() == Sort.Direction.ASC
                ? Comparator.comparing(CommentResponseDto::getCreatedAt)
                : Comparator.comparing(CommentResponseDto::getCreatedAt).reversed();

        parentComments.sort(comparator);

        // 5. 각 대댓글 그룹을 오래된 순으로 정렬
        for (List<CommentResponseDto> replies : repliesByParentId.values()) {
            replies.sort(Comparator.comparing(CommentResponseDto::getCreatedAt));
        }

        // 6. 부모 댓글과 대댓글을 순서대로 조합 (플랫 리스트)
        List<CommentResponseDto> orderedComments = new ArrayList<>();
        for (CommentResponseDto parent : parentComments) {
            // 부모 댓글 추가
            orderedComments.add(parent);

            // 해당 부모의 대댓글들 추가
            List<CommentResponseDto> replies = repliesByParentId.get(parent.getId());
            if (replies != null && !replies.isEmpty()) {
                orderedComments.addAll(replies);
            }
        }

        // 7. 애플리케이션 레벨에서 페이징 처리
        int totalElements = orderedComments.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), totalElements);

        // 범위를 벗어나는 경우 빈 리스트
        if (start >= totalElements) {
            return new PageImpl<>(Collections.emptyList(), pageable, totalElements);
        }

        List<CommentResponseDto> pageContent = orderedComments.subList(start, end);

        return new PageImpl<>(pageContent, pageable, totalElements);
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

        // 5. 활동 로그 저장
        activityLogService.log(ActivityType.COMMENT_CREATED, user, task, task.getTitle());

        return CreateCommentResponse.from(savedComment);
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

    @Transactional
    public UpdateCommentResponse updateComment(Long taskId, Long commentId, @Valid UpdateCommentRequest request, AuthUserDto authUserDto) {

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

        // 5. 활동 로그 저장
        activityLogService.log(ActivityType.COMMENT_UPDATED, comment.getUser(), comment.getTask());

        // 6. 변경 감지로 자동 저장됨 (Dirty Checking)
        return UpdateCommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long taskId, Long commentId, AuthUserDto authUserDto) {

        // 1. Task 존재 여부 확인
        if (!taskRepository.existsById(taskId)) {
            throw new CustomException(ErrorMessage.NOT_FOUND_TASK);
        }

        // 2. 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_COMMENT));

        // 3. 권한 확인: 댓글 작성자만 삭제 가능
        if (!comment.getUser().getId().equals(authUserDto.getId())) {
            throw new CustomException(ErrorMessage.NOT_COMMENT_DELETE_AUTHORIZED);
        }

        // 4. 부모 댓글인지 확인하고 삭제 처리
        if (comment.getParent() == null) {
            // 부모 댓글 → 자식 댓글도 함께 삭제
            deleteCommentWithReplies(comment);
        } else {
            // 대댓글 → 해당 댓글만 삭제
            comment.delete();
        }

        // 5. 활동 로그 저장
        activityLogService.log(ActivityType.COMMENT_DELETED, comment.getUser(), comment.getTask());
    }

    /**
     * 부모 댓글과 모든 하위 대댓글 삭제
     */
    private void deleteCommentWithReplies(Comment parentComment) {
        // 1. 자식 댓글 일괄 삭제 (한 번의 쿼리로)
        commentRepository.softDeleteRepliesByParentId(parentComment.getId());

        // 2. 부모 댓글 삭제
        parentComment.delete();
    }

}
