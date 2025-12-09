package com.example.outsourcing_taskflow.domain.user.repository;

import com.example.outsourcing_taskflow.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
