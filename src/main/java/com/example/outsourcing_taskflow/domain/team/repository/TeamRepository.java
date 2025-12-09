package com.example.outsourcing_taskflow.domain.team.repository;

import com.example.outsourcing_taskflow.common.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
