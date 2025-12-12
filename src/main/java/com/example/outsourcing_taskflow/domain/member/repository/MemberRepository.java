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

    // 해당 teamId를 가진 모든 Member 목록을 조회하는 메서드
    List<Member> findAllByTeamId(Long teamId);

    // 해당 teamId를 가진 멤버가 존재하는지 확인하는 메서드
    boolean existsByTeamId(Long teamId);

    // 이미 가입된 멤버인지 확인하는 메서드
    boolean existsByTeamAndUser(Team team, User user);

    // Team 엔티티와 User 엔티티에 해당하는 Member 엔티티를 조회
    Optional<Member> findByTeamAndUser(Team team, User user);

    // 추가 가능한 사용자 조회
    @Query("SELECT u FROM User u WHERE u.id NOT IN " +
            "(SELECT m.user.id FROM Member m WHERE m.team.id = :teamId)")
    List<User> findUsersNotInTeam(@Param("teamId") Long teamId);
}
