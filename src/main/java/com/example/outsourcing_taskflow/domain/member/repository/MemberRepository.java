package com.example.outsourcing_taskflow.domain.member.repository;

import com.example.outsourcing_taskflow.common.entity.Member;
import com.example.outsourcing_taskflow.common.entity.Team;
import com.example.outsourcing_taskflow.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    boolean existsByTeamId(Long teamId);

    boolean existsByTeamAndUser(Team team, User user);

    Optional<Member> findByTeamAndUser(Team team, User user);

    @Query("SELECT m FROM Member m JOIN FETCH m.user WHERE m.team.id = :teamId")
    List<Member> findAllByTeamIdWithUser(@Param("teamId") Long teamId);

    @Query("SELECT m FROM Member m JOIN FETCH m.user")
    List<Member> findAllWithUser();
}
