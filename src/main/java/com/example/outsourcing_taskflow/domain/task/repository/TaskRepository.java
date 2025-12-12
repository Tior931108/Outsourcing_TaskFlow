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
}
