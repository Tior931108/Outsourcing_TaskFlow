package com.example.outsourcing_taskflow.domain.team.repository;

import com.example.outsourcing_taskflow.common.entity.Team;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    // 팀 이름으로 조회하는 메서드
    Optional<Team> findByTeamName(String teamName);
    Optional<Team> findByIdAndIsDeleted(Long id, IsDeleted isDeleted);
    List<Team> findAllByIsDeleted(IsDeleted isDeleted);
}