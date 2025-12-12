package com.example.outsourcing_taskflow.domain.task.repository;

import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // 상태로 작업을 검색
    Page<Task> findByStatus(TaskStatusEnum status, Pageable pageable);

    // 담당자로 작업을 검색
    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);

    // 상태+담당자로 작업을 검색
    Page<Task> findByStatusAndAssigneeId(TaskStatusEnum status, Long assigneeId, Pageable pageable);


    // - Search By Keyword
    @Query("""
        select t
        from Task t
        where lower(t.title) like lower(concat('%', :keyword, '%'))
        """)
    List<Task> searchByKeyword(@Param("keyword") String keyword);


    /**
     * 대시보드 통계용 메소드
     */

    // 전체 작업 수 (삭제되지 않은 것만)
    long countByIsDeleted(IsDeleted isDeleted);

    // 상태별 작업 수
    long countByStatusAndIsDeleted(TaskStatusEnum status, IsDeleted isDeleted);

    // 기한 초과 작업 수 (TODO, IN_PROGRESS 상태이면서 마감일이 지난 것)
    @Query("SELECT COUNT(t) FROM Task t " +
            "WHERE t.dueDate < :now " +
            "AND t.status IN (com.example.outsourcing_taskflow.common.enums.TaskStatusEnum.TODO, " +
            "                 com.example.outsourcing_taskflow.common.enums.TaskStatusEnum.IN_PROGRESS) " +
            "AND t.isDeleted = :isDeleted")
    long countOverdueTasks(@Param("now") LocalDateTime now, @Param("isDeleted") IsDeleted isDeleted);

    // 특정 사용자의 전체 작업 수
    long countByAssigneeIdAndIsDeleted(Long userId, IsDeleted isDeleted);

    // 특정 사용자의 완료된 작업 수
    long countByAssigneeIdAndStatusAndIsDeleted(Long userId, TaskStatusEnum status, IsDeleted isDeleted);


    /**
     * 작업 요약용 메소드
     */

    //오늘 마감인 내 작업 조회 (오늘 00:00 ~ 23:59:59)
    @Query("SELECT t FROM Task t " +
            "WHERE t.assignee.id = :userId " +
            "AND t.dueDate >= :startOfDay " +
            "AND t.dueDate < :endOfDay " +
            "AND t.status IN (com.example.outsourcing_taskflow.common.enums.TaskStatusEnum.TODO, " +
            "                 com.example.outsourcing_taskflow.common.enums.TaskStatusEnum.IN_PROGRESS) " +
            "AND t.isDeleted = :isDeleted " +
            "ORDER BY t.priority DESC, t.dueDate ASC")
    List<Task> findTodayTasksByUserId(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("isDeleted") IsDeleted isDeleted
    );

    //다가오는 내 작업 조회 (오늘 이후 ~ 7일 이내)
    @Query("SELECT t FROM Task t " +
            "WHERE t.assignee.id = :userId " +
            "AND t.dueDate >= :tomorrow " +
            "AND t.dueDate < :weekLater " +
            "AND t.status IN (com.example.outsourcing_taskflow.common.enums.TaskStatusEnum.TODO, " +
            "                 com.example.outsourcing_taskflow.common.enums.TaskStatusEnum.IN_PROGRESS) " +
            "AND t.isDeleted = :isDeleted " +
            "ORDER BY t.dueDate ASC, t.priority DESC")
    List<Task> findUpcomingTasksByUserId(
            @Param("userId") Long userId,
            @Param("tomorrow") LocalDateTime tomorrow,
            @Param("weekLater") LocalDateTime weekLater,
            @Param("isDeleted") IsDeleted isDeleted
    );

    //기한 초과된 내 작업 조회 (오늘 이전)
    @Query("SELECT t FROM Task t " +
            "WHERE t.assignee.id = :userId " +
            "AND t.dueDate < :startOfDay " +
            "AND t.status IN (com.example.outsourcing_taskflow.common.enums.TaskStatusEnum.TODO, " +
            "                 com.example.outsourcing_taskflow.common.enums.TaskStatusEnum.IN_PROGRESS) " +
            "AND t.isDeleted = :isDeleted " +
            "ORDER BY t.dueDate ASC")
    List<Task> findOverdueTasksByUserId(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("isDeleted") IsDeleted isDeleted
    );

    /**
     * 주간 작업 통계 메소드
     */

    // 특정 날짜에 생성된 작업 수 (createdAt 기준)
    @Query("SELECT COUNT(t) FROM Task t " +
            "WHERE t.createdAt >= :startOfDay " +
            "AND t.createdAt < :endOfDay " +
            "AND t.isDeleted = :isDeleted")
    long countTasksCreatedOnDate(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("isDeleted") IsDeleted isDeleted
    );

    // 특정 날짜에 완료된 작업 수 (updatedAt 기준, DONE 상태)
    @Query("SELECT COUNT(t) FROM Task t " +
            "WHERE t.updatedAt >= :startOfDay " +
            "AND t.updatedAt < :endOfDay " +
            "AND t.status = com.example.outsourcing_taskflow.common.enums.TaskStatusEnum.DONE " +
            "AND t.isDeleted = :isDeleted")
    long countTasksCompletedOnDate(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("isDeleted") IsDeleted isDeleted
    );
}
