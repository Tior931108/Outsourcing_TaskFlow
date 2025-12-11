package com.example.outsourcing_taskflow.domain.team.repository;

import com.example.outsourcing_taskflow.common.entity.Team;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    // 팀 이름으로 조회하는 메서드
    Optional<Team> findByTeamName(String teamName);

    // - Search By Keyword
    @Query("""
        select t
        from Team t
        where lower(t.teamName) like lower(concat('%', :keyword, '%'))
        """)
    List<Team> searchByKeyword(@Param("keyword") String keyword);
}