package com.example.outsourcing_taskflow.domain.task.service;

import com.example.outsourcing_taskflow.common.config.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import com.example.outsourcing_taskflow.domain.task.dto.request.CreateTaskRequest;
import com.example.outsourcing_taskflow.domain.task.dto.request.UpdateTaskRequest;
import com.example.outsourcing_taskflow.domain.task.dto.request.UpdateTaskStatusRequest;
import com.example.outsourcing_taskflow.domain.task.dto.response.CreateTaskResponse;
import com.example.outsourcing_taskflow.domain.task.dto.response.GetTaskResponse;
import com.example.outsourcing_taskflow.domain.task.dto.response.UpdateTaskResponse;
import com.example.outsourcing_taskflow.domain.task.dto.response.UpdateTaskStatusResponse;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        // 레포지토리에서 delete처리된거 아예 처음부터 안가져오도록
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_TASK));

        // 논리 삭제된 작업은 제외
//        if (task.isDeleted()) {
        if (task.getIsDeleted() == IsDeleted.TRUE) {
            throw new CustomException(ErrorMessage.NOT_FOUND_TASK);
        }

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

//        // DB 쿼리 선택
//        if (statusEnum != null && assigneeId != null) {
//            // 상태 + 담당자
//            tasks = taskRepository.findByStatusAndAssigneeId(statusEnum, assigneeId, pageable);
//        } else if (statusEnum != null) {
//            // 상태만
//            tasks = taskRepository.findByStatus(statusEnum, pageable);
//        } else if (assigneeId != null) {
//            // 담당자만
//            tasks = taskRepository.findByAssigneeId(assigneeId, pageable);
//        } else { // 아무 조건 없이 전체 조회
//            tasks = taskRepository.findAll(pageable);
//        }

        // 작업상태와 담당자의 값 여부에 따른 조회 + 삭제되지 않은 작업만
        if (statusEnum != null && assigneeId != null) {
            tasks = taskRepository.findByStatusAndAssigneeIdAndIsDeleted(
                    statusEnum, assigneeId, IsDeleted.FALSE, pageable);
        } else if (statusEnum != null) {
            tasks = taskRepository.findByStatusAndIsDeleted(
                    statusEnum, IsDeleted.FALSE, pageable);
        } else if (assigneeId != null) {
            tasks = taskRepository.findByAssigneeIdAndIsDeleted(
                    assigneeId, IsDeleted.FALSE, pageable);
        } else {
            tasks = taskRepository.findByIsDeleted(IsDeleted.FALSE, pageable);
        }

        // search - keyword
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

    public UpdateTaskResponse updateTask(Long taskId, UpdateTaskRequest request, AuthUserDto authUserDto) {

        // 작업 가져오기 + 404 Not Found: 작업을 찾을 수 없음
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_TASK));

        // 논리 삭제된 작업은 제외
//        if (task.isDeleted()) {
        if (task.getIsDeleted() == IsDeleted.TRUE) {
            throw new CustomException(ErrorMessage.NOT_FOUND_TASK);
        }

        // [현재 접속중인 사용자] = ![작업 담당자] && ![관리자] -> 403 Forbidden: 수정 권한 없음
        // authUserDto에 현재 접속중인 사용자 id, username, role이 담겨있음
        boolean isAssignee = authUserDto.getId().equals(task.getAssignee().getId());
        boolean isAdmin = authUserDto.getRole().equals(UserRoleEnum.ADMIN.getRole()); // JWT로부터 role을 String 권한으로 주입할 때 "ROLE_ADMIN" 형태인지 확인

        // 담당자가 아니고 + 관리자가 아니면 -> 403 Forbidden
        if (!isAssignee && !isAdmin) {
            throw new CustomException(ErrorMessage.NOT_MODIFY_AUTHORIZED);
        }

        // 요청에서 받아온 담당자 id에 매칭되는 유저 찾기 + 예외처리
        User newAssignee = null;
        if (request.getAssigneeId() != null) {
            newAssignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_TASK_USER));
        }

        // 엔티티 업데이트
        task.update(request, newAssignee);
        // 변경 감지(Dirty Checking)에 의해 자동 업데이트
        return UpdateTaskResponse.from(task);
    }

// ---------------------------------------------------------------------------------------------------------------------

    public void deleteTask(AuthUserDto authUserDto, Long taskId) {

        // 작업 조회 + 404 Not Found: 작업을 찾을 수 없음
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_TASK));

        // 논리 삭제된 작업은 제외
//        if (task.isDeleted()) {
        if (task.getIsDeleted() == IsDeleted.TRUE) {
            throw new CustomException(ErrorMessage.NOT_FOUND_TASK);
        }

        // [현재 접속중인 사용자] = ![작업 담당자] && ![관리자] -> 403 Forbidden: 삭제 권한 없음
        boolean isAssignee = authUserDto.getId().equals(task.getAssignee().getId());
        boolean isAdmin = authUserDto.getRole().equals(UserRoleEnum.ADMIN.getRole()); // JWT로부터 role을 String 권한으로 주입할 때 "ROLE_ADMIN" 형태인지 확인
        if (!isAssignee && !isAdmin) {
            throw new CustomException(ErrorMessage.NOT_TASK_DELETE_AUTHORIZED);
        }

        task.softDelete();

    }

    public UpdateTaskStatusResponse updateTaskStatus(Long taskId, UpdateTaskStatusRequest request) {

        // 작업 조회 + 404 Not Found: 작업을 찾을 수 없음
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_TASK));

        // 논리 삭제된 작업은 제외
//        if (task.isDeleted()) {
        if (task.getIsDeleted() == IsDeleted.TRUE) {
            throw new CustomException(ErrorMessage.NOT_FOUND_TASK);
        }

        // request의 status값이 null이거나 _TODO/IN_PROGRESS/DONE 이 아닌 경우
        // null의 경우 서비스단에서 잡아서 처리가능, 하지만 잘못된 Enum값이 서비스로 들어올 경우는 없음(그 전에 처리가 되므로, 이 부분을 따로 설정하려면 전역에러핸들링 필요)
        // 400 Bad Request: 잘못된 상태 값
        if (request.getStatus() == null) {
            throw new CustomException(ErrorMessage.NOT_CORRECT_TASK_STATUS);
        }

        // 상태 변경: _TODO → IN_PROGRESS → DONE 순차적 변경만 허용
        TaskStatusEnum currentStatus = task.getStatus();
        TaskStatusEnum newStatus = request.getStatus();
        boolean isValidTransition =
                (currentStatus == TaskStatusEnum.TODO && newStatus == TaskStatusEnum.IN_PROGRESS) ||
                        (currentStatus == TaskStatusEnum.IN_PROGRESS && newStatus == TaskStatusEnum.DONE);
        if (!isValidTransition) {
            throw new CustomException(ErrorMessage.NOT_CORRECT_TASK_STATUS);
        }

        task.updateStatus(newStatus);

        return UpdateTaskStatusResponse.from(task);
    }

// ---------------------------------------------------------------------------------------------------------------------

}
