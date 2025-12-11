package com.example.outsourcing_taskflow.domain.task.repository;

import com.example.outsourcing_taskflow.common.entity.Task;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
