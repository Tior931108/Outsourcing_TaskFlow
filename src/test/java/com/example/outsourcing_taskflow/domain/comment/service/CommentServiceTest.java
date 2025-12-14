package com.example.outsourcing_taskflow.domain.comment.service;

import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.common.enums.TaskPriorityEnum;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import com.example.outsourcing_taskflow.domain.activitylog.service.ActivityLogService;
import com.example.outsourcing_taskflow.domain.comment.model.dto.CommentResponseDto;
import com.example.outsourcing_taskflow.domain.comment.repository.CommentRepository;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CommentService commentService;

    private User testUser1;
    private User testUser2;
    private Task testTask;
    private Long taskId;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성 (생성자 사용)
        testUser1 = new User(
                "testuser1",
                "password123",
                "테스트유저1",
                "testuser1@example.com"
        );
        ReflectionTestUtils.setField(testUser1, "id", 1L);

        testUser2 = new User(
                "testuser2",
                "password456",
                "테스트유저2",
                "testuser2@example.com"
        );
        ReflectionTestUtils.setField(testUser2, "id", 2L);

        // 테스트용 작업 생성
        testTask = new Task(
                "테스트 작업",
                "작업 설명",
                TaskStatusEnum.TODO,
                TaskPriorityEnum.HIGH,
                testUser1,
                LocalDateTime.now().plusDays(7)
        );
        ReflectionTestUtils.setField(testTask, "id", 1L);
        taskId = 1L;
    }

    @Test
    @DisplayName("댓글과 대댓글을 계층 구조로 조회할 수 있다.")
    void getComments_댓글과_대댓글_계층구조로_조회할_수_있다_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 댓글 DTO 리스트 생성 (시간순)
        LocalDateTime now = LocalDateTime.now();
        List<CommentResponseDto> allComments = Arrays.asList(
                // 부모 댓글 1 (가장 오래된)
                new CommentResponseDto(
                        1L, "첫 번째 댓글", taskId, 1L,
                        "testuser1", "테스트유저1", "testuser1@example.com",
                        UserRoleEnum.USER, null,
                        now.minusHours(3), now.minusHours(3)
                ),
                // 부모 댓글 2
                new CommentResponseDto(
                        2L, "두 번째 댓글", taskId, 2L,
                        "testuser2", "테스트유저2", "testuser2@example.com",
                        UserRoleEnum.USER, null,
                        now.minusHours(2), now.minusHours(2)
                ),
                // 대댓글 2-1
                new CommentResponseDto(
                        3L, "두 번째 댓글의 답글", taskId, 1L,
                        "testuser1", "테스트유저1", "testuser1@example.com",
                        UserRoleEnum.USER, 2L,
                        now.minusHours(1).minusMinutes(30), now.minusHours(1).minusMinutes(30)
                ),
                // 부모 댓글 3 (가장 최신)
                new CommentResponseDto(
                        4L, "세 번째 댓글", taskId, 1L,
                        "testuser1", "테스트유저1", "testuser1@example.com",
                        UserRoleEnum.USER, null,
                        now.minusMinutes(30), now.minusMinutes(30)
                ),
                // 대댓글 3-1
                new CommentResponseDto(
                        5L, "세 번째 댓글의 첫 답글", taskId, 2L,
                        "testuser2", "테스트유저2", "testuser2@example.com",
                        UserRoleEnum.USER, 4L,
                        now.minusMinutes(20), now.minusMinutes(20)
                ),
                // 대댓글 3-2
                new CommentResponseDto(
                        6L, "세 번째 댓글의 두 번째 답글", taskId, 1L,
                        "testuser1", "테스트유저1", "testuser1@example.com",
                        UserRoleEnum.USER, 4L,
                        now.minusMinutes(10), now.minusMinutes(10)
                )
        );

        given(taskRepository.existsById(taskId)).willReturn(true);
        given(commentRepository.findAllCommentsByTaskId(taskId, IsDeleted.FALSE))
                .willReturn(allComments);

        // when
        Page<CommentResponseDto> result = commentService.getComments(taskId, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(6);
        assertThat(result.getTotalElements()).isEqualTo(6);

        // 최신순 정렬 확인: 부모3 -> 대댓글3-1 -> 대댓글3-2 -> 부모2 -> 대댓글2-1 -> 부모1
        List<CommentResponseDto> content = result.getContent();
        assertThat(content.get(0).getId()).isEqualTo(4L); // 부모3 (최신)
        assertThat(content.get(1).getId()).isEqualTo(5L); // 대댓글 3-1
        assertThat(content.get(2).getId()).isEqualTo(6L); // 대댓글 3-2
        assertThat(content.get(3).getId()).isEqualTo(2L); // 부모2
        assertThat(content.get(4).getId()).isEqualTo(3L); // 대댓글 2-1
        assertThat(content.get(5).getId()).isEqualTo(1L); // 부모1 (가장 오래된)

        // 계층 구조 확인
        assertThat(content.get(0).getParentId()).isNull(); // 부모
        assertThat(content.get(1).getParentId()).isEqualTo(4L); // 부모3의 자식
        assertThat(content.get(2).getParentId()).isEqualTo(4L); // 부모3의 자식

        verify(taskRepository, times(1)).existsById(taskId);
        verify(commentRepository, times(1)).findAllCommentsByTaskId(taskId, IsDeleted.FALSE);
    }

    @Test
    @DisplayName("댓글 페이징처리 성공할 수 있다.(2페이지 조회)")
    void getComments_댓글_페이징처리_성공할_수_있다_success() {
        // given
        Pageable pageable = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "createdAt"));

        LocalDateTime now = LocalDateTime.now();
        List<CommentResponseDto> allComments = Arrays.asList(
                new CommentResponseDto(1L, "댓글1", taskId, 1L, "testuser1", "테스트유저1",
                        "testuser1@example.com", UserRoleEnum.USER, null, now.minusHours(5), now.minusHours(5)),
                new CommentResponseDto(2L, "댓글2", taskId, 1L, "testuser1", "테스트유저1",
                        "testuser1@example.com", UserRoleEnum.USER, null, now.minusHours(4), now.minusHours(4)),
                new CommentResponseDto(3L, "댓글3", taskId, 1L, "testuser1", "테스트유저1",
                        "testuser1@example.com", UserRoleEnum.USER, null, now.minusHours(3), now.minusHours(3)),
                new CommentResponseDto(4L, "댓글4", taskId, 1L, "testuser1", "테스트유저1",
                        "testuser1@example.com", UserRoleEnum.USER, null, now.minusHours(2), now.minusHours(2)),
                new CommentResponseDto(5L, "댓글5", taskId, 1L, "testuser1", "테스트유저1",
                        "testuser1@example.com", UserRoleEnum.USER, null, now.minusHours(1), now.minusHours(1))
        );

        given(taskRepository.existsById(taskId)).willReturn(true);
        given(commentRepository.findAllCommentsByTaskId(taskId, IsDeleted.FALSE))
                .willReturn(allComments);

        // when
        Page<CommentResponseDto> result = commentService.getComments(taskId, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2); // 3개씩, 2페이지 → 2개 남음
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(1); // 2페이지 (0-indexed)

        // 2페이지 내용: 댓글2, 댓글1 (최신순)
        List<CommentResponseDto> content = result.getContent();
        assertThat(content.get(0).getId()).isEqualTo(2L);
        assertThat(content.get(1).getId()).isEqualTo(1L);
    }
}