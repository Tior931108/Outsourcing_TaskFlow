package com.example.outsourcing_taskflow.domain.user.repository;

import com.example.outsourcing_taskflow.common.entity.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserName(String userName);

    boolean existsByEmail(@Email(message = "올바른 이메일 형식이 아닙니다.") String email);
}
