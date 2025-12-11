package com.example.outsourcing_taskflow.domain.task.repository;

import com.example.outsourcing_taskflow.common.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    //

    // - Search By Keyword
    @Query("""
        select t
        from Task t
        where lower(t.title) like lower(concat('%', :keyword, '%'))
        """)
    List<Task> searchByKeyword(@Param("keyword") String keyword);
}
