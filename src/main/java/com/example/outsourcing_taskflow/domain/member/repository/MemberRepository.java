package com.example.outsourcing_taskflow.domain.member.repository;

import com.example.outsourcing_taskflow.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 해당 teamId를 가진 모든 Member 목록을 조회하는 메서드
    List<Member> findAllByTeamId(Long teamId);

    // 해당 teamId를 가진 멤버가 존재하는지 확인하는 메서드
    boolean existsByTeamId(Long teamId);
}
