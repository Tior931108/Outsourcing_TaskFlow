package com.example.outsourcing_taskflow.domain.team.repository;

import com.example.outsourcing_taskflow.common.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findById(Long id);

    Optional<Team> findByTeamNameAndIsDeletedFalse(String teamName);

    @Query("""
        select t
        from Team t
        where lower(t.teamName) like lower(concat('%', :keyword, '%'))
        """)
    List<Team> searchByKeyword(@Param("keyword") String keyword);


}