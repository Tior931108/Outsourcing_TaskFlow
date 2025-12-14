package com.example.outsourcing_taskflow.common.utils;

import com.example.outsourcing_taskflow.common.entity.*;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class Administrator implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        // ===== 관리자 계정 생성 =====
        // 관리자 권한 변경은 없기에 초기 프로젝트 실행시 관리자 계정 데이터 생성

        User admin = new User("admin", "admin@example.com",
                passwordEncoder.encode("admin123@!"), "관리자");
        admin.updateAdminRole();
        userRepository.save(admin);

    }
}