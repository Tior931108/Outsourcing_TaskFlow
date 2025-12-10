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

    public GetTaskResponse getTask(Long taskId) {

        // 작업 가져오기 + 404 Not Found: 작업을 찾을 수 없음
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_TASK));

        return GetTaskResponse.from(task);
    }

    public PageResponse<GetTaskResponse> getAllTasks(int page, int size, String status, String search, Long assigneeId) {

        // page, size 유효성 검사
        if (page < 0 || size <= 0) {
            // 지금 당장 페이징 관련 커스텀 예외가 없으니 임시로..
            throw new IllegalArgumentException("페이지가 '0'미만이거나 사이즈가 '0'이하일 수 없습니다.");
        }

        // pageable : 페이징 정보를 담고 있는 객체
        Pageable pageable = PageRequest.of(page, size);
        // JPA Specification : DB 쿼리의 조건을 간단히 Spec으로 작성하여 날릴 수 있게 해줌, Repository인터페이스에 선언해서 씀
        //Specification<Task> spec = Specification.where(null); 다른 좋은 방법은 없나..
        
        // 작업 상태 유효성 검사
        if (status != null) {
            try {
                TaskStatusEnum statusEnum = TaskStatusEnum.valueOf(status.toUpperCase());   // toUpperCase() 사실상 불필요..?

            } catch (IllegalArgumentException e) {
                throw new CustomException(ErrorMessage.NOT_CORRECT_TASK_STATUS);
            }
        }

        // 검색어


        // 담당자
        
        
        // 조회
        
        
        // 반환
        return PageResponse.success("작업 목록 조회 성공", responsePage);

    }

}
