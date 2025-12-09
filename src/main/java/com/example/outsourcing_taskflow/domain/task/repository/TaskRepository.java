package com.example.outsourcing_taskflow.domain.task.repository;

import com.example.outsourcing_taskflow.common.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
