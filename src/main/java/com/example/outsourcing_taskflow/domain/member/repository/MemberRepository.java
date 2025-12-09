package com.example.outsourcing_taskflow.domain.member.repository;

import com.example.outsourcing_taskflow.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
