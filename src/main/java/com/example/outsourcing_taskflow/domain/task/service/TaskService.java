package com.example.outsourcing_taskflow.domain.task.service;

import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import com.example.outsourcing_taskflow.common.response.PageResponse;
import com.example.outsourcing_taskflow.domain.task.dto.request.CreateTaskRequest;
import com.example.outsourcing_taskflow.domain.task.dto.response.CreateTaskResponse;
import com.example.outsourcing_taskflow.domain.task.dto.response.GetTaskResponse;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

// ---------------------------------------------------------------------------------------------------------------------
    public CreateTaskResponse createTask(CreateTaskRequest request) {

        // 400 Bad Request: 필수 필드 누락
        if (request.getTitle() == null || request.getAssigneeId() == null) {
            throw new CustomException(ErrorMessage.MUST_TITLE_TASK_USER);
        }

        // 담당자 가져오기 + 404 Not Found: 담당자를 찾을 수 없음
        User user = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_TASK_USER));

        Task createdTask = new Task(
                request.getTitle(),
                request.getDescription(),
                TaskStatusEnum.TODO,
                request.getPriority(),
                user,
                request.getDueDate()
        );

        Task savedTask = taskRepository.save(createdTask);

        return CreateTaskResponse.from(savedTask);
    }

// ---------------------------------------------------------------------------------------------------------------------

    public GetTaskResponse getTask(Long taskId) {

        // 작업 가져오기 + 404 Not Found: 작업을 찾을 수 없음
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_TASK));

        return GetTaskResponse.from(task);
    }

// ---------------------------------------------------------------------------------------------------------------------

    public Page<GetTaskResponse> getAllTasks(int page, int size, String status, String search, Long assigneeId) {

        // page, size 유효성 검사
        if (page < 0 || size <= 0) {
            throw new CustomException(ErrorMessage.NOT_CORRECT_PARAMETER);
        }

        // pageable : 페이징 정보를 담고 있는 객체
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks;
        
        // 작업 상태 유효성 검사
        TaskStatusEnum statusEnum = null;
        if (status != null) {
            try {
                statusEnum = TaskStatusEnum.valueOf(status.toUpperCase());   // toUpperCase() 사실상 불필요..?

            } catch (IllegalArgumentException e) {
                throw new CustomException(ErrorMessage.NOT_CORRECT_PARAMETER);
            }
        }

        // DB 쿼리 선택
        if (statusEnum != null && assigneeId != null) {
            // 상태 + 담당자
            tasks = taskRepository.findByStatusAndAssigneeId(statusEnum, assigneeId, pageable);
        } else if (statusEnum != null) {
            // 상태만
            tasks = taskRepository.findByStatus(statusEnum, pageable);
        } else if (assigneeId != null) {
            // 담당자만
            tasks = taskRepository.findByAssigneeId(assigneeId, pageable);
        } else { // 아무 조건 없이 전체 조회
            tasks = taskRepository.findAll(pageable);
        }

        // search
        if (search != null && !search.isEmpty()) {
            String keyword = search.toLowerCase();

            // 작업의 제목, 내용에 검색 키워드 일치 시 리스트에 담기
            var filteredList = tasks.getContent().stream()
                    .filter(task ->
                            (task.getTitle() != null && task.getTitle().toLowerCase().contains(keyword)) ||
                                    (task.getDescription() != null && task.getDescription().toLowerCase().contains(keyword))
                    )
                    .toList();

            // PageImpl 로 다시 감싸기
            tasks = new PageImpl<>(filteredList, pageable, filteredList.size());
        }
        
        // DTO 매핑
        return tasks.map(GetTaskResponse::from);

    }

// ---------------------------------------------------------------------------------------------------------------------

}
