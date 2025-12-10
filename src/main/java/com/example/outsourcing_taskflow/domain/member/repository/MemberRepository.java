package com.example.outsourcing_taskflow.domain.member.repository;

import com.example.outsourcing_taskflow.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllWithUserByTeamId(Long teamId);

    List<Member> findAllByTeamId(Long id);
}
