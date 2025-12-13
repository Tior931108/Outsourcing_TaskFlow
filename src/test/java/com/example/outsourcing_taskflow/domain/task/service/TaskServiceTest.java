package com.example.outsourcing_taskflow.domain.task.service;

import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.TaskPriorityEnum;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.domain.activitylog.service.ActivityLogService;
import com.example.outsourcing_taskflow.domain.task.dto.request.CreateTaskRequest;
import com.example.outsourcing_taskflow.domain.task.dto.response.CreateTaskResponse;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActivityLogService activityLogService; // createTask() 안에서 호출되기 때문에 필수

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_메서드_호출시_작업이_생성된다() {
        // ------------------ Given
        CreateTaskRequest request = new CreateTaskRequest();
        ReflectionTestUtils.setField(request, "title", "테스트 작업");
        ReflectionTestUtils.setField(request, "description", "테스트 내용");
        ReflectionTestUtils.setField(request, "priority", TaskPriorityEnum.HIGH);
        ReflectionTestUtils.setField(request, "assigneeId", 1L);
        ReflectionTestUtils.setField(request, "dueDate", LocalDateTime.now().plusDays(1));

        User user = createUser(1L); // protected 생성자를 가진 User 객체 생성, 테스트용 최소 객체...

        // 실제 DB 조회를 하지않고, findById -> 테스트용 유저객체 / save -> 들어온 Task 그대로 반환
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // void 메서드는 when().thenReturn() 사용 불가
        //로그 호출만 하고 아무 일도 안 하게 처리
        doNothing().when(activityLogService).log(any(), any(), any(), any());

        // ------------------ When
        CreateTaskResponse response = taskService.createTask(request);

        // ------------------ Then
        assertNotNull(response);    // assertThat(result).isNotNull();
        assertEquals("테스트 작업", response.getTitle());    // assertThat(response.getTitle()).isEqualTo(request.getTitle)
        assertEquals(TaskStatusEnum.TODO, response.getStatus());   // assertThat(response.getStatus()).isEqualTo(request.getStatus)
        // taskRepository가 호출된 횟수:1회 및 save()인지 검사
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    // 사용자 생성
    private User createUser(Long id) {
        try {
            Constructor<User> constructor = User.class.getDeclaredConstructor();

            // Constructor : protected User() 생성자 접근 허용
            constructor.setAccessible(true);

            // User 인스턴스 생성
            User user = constructor.newInstance();

            // JPA가 설정하는 PK를 테스트에서 수동 주입
            ReflectionTestUtils.setField(user, "id", id);

            return user;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}