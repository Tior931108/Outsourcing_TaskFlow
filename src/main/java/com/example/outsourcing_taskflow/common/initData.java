package com.example.outsourcing_taskflow.common;

import com.example.outsourcing_taskflow.common.entity.*;
import com.example.outsourcing_taskflow.common.enums.TaskPriorityEnum;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.domain.activitylog.repository.ActivityLogRepository;
import com.example.outsourcing_taskflow.domain.member.repository.MemberRepository;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.team.repository.TeamRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class initData {

    // 레파지토리 의존성 주입하는 공간
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;
    private final ActivityLogRepository activityLogRepository;

    @PostConstruct
    @Transactional
    public void init() {
        // 기존 데이터가 있으면 초기화하지 않음
        if (userRepository.count() > 0) {
            return;
        }

        // ===== 1. 사용자 생성 =====
        User user1 = new User(
                "johndoe",
                "john@example.com",
                passwordEncoder.encode("Password123"),
                "John Doe"
        );
        userRepository.save(user1);

        User user2 = new User(
                "janedoe",
                "jane@example.com",
                passwordEncoder.encode("Password123"),
                "Jane Doe"
        );
        userRepository.save(user2);

        User user3 = new User(
                "leeyounghe",
                "younghee@example.com",
                passwordEncoder.encode("Password123"),
                "이영희"
        );
        userRepository.save(user3);

        log.info("userRepository.count() = " + userRepository.count());

        // ===== 2. Team 생성 =====
        Team team1 = new Team("FE 연동", "프론트엔드 연동 팀");
        teamRepository.save(team1);

        Team team2 = new Team("FE 연동 테스트", "프론트엔드 연동 테스트 팀");
        teamRepository.save(team2);

        Team team3 = new Team("대시보드", "대시보드 개발 팀");
        teamRepository.save(team3);

        Team team4 = new Team("API 문서 작성", "API 문서화 팀");
        teamRepository.save(team4);

        Team team5 = new Team("테스트", "QA 테스트 팀");
        teamRepository.save(team5);

        log.info("teamRepository.count() = " + teamRepository.count());

        // ===== 3. Member 생성 =====
        memberRepository.save(new Member(team1, user1));
        memberRepository.save(new Member(team3, user1));
        memberRepository.save(new Member(team1, user2));
        memberRepository.save(new Member(team2, user2));
        memberRepository.save(new Member(team4, user2));
        memberRepository.save(new Member(team2, user3));
        memberRepository.save(new Member(team5, user3));

        log.info("memberRepository.count() = " + memberRepository.count());

        // ===== 4. Task 생성 =====
        Task task1 = new Task(
                "대시보드 UI 디자인",
                "대시보드를 위한 와이어프레임을 제작합니다",
                TaskStatusEnum.TODO,
                TaskPriorityEnum.MEDIUM,
                user1,
                LocalDateTime.now().plusDays(5)
        );
        taskRepository.save(task1);

        Task task2 = new Task(
                "백엔드 API 개발",
                "RESTful API를 구현합니다",
                TaskStatusEnum.TODO,
                TaskPriorityEnum.HIGH,
                user2,
                LocalDateTime.now().plusDays(3)
        );
        taskRepository.save(task2);

        Task task3 = new Task(
                "회원가입 기능 구현",
                "사용자 회원가입 기능을 구현합니다",
                TaskStatusEnum.TODO,
                TaskPriorityEnum.HIGH,
                user3,
                LocalDateTime.now().plusDays(4)
        );
        taskRepository.save(task3);

        Task task4 = new Task(
                "프론트엔드 개발",
                "React를 사용하여 사용자 인터페이스를 구현합니다",
                TaskStatusEnum.IN_PROGRESS,
                TaskPriorityEnum.HIGH,
                user1,
                LocalDateTime.now().plusDays(7)
        );
        taskRepository.save(task4);

        Task task5 = new Task(
                "데이터베이스 최적화",
                "쿼리 성능을 개선합니다",
                TaskStatusEnum.IN_PROGRESS,
                TaskPriorityEnum.MEDIUM,
                user3,
                LocalDateTime.now().plusDays(10)
        );
        taskRepository.save(task5);

        Task task6 = new Task(
                "데이터베이스 설계",
                "ERD를 작성하고 테이블을 설계합니다",
                TaskStatusEnum.DONE,
                TaskPriorityEnum.HIGH,
                user2,
                LocalDateTime.now().minusDays(5)
        );
        taskRepository.save(task6);

        Task task7 = new Task(
                "요구사항 분석",
                "프로젝트 요구사항을 분석합니다",
                TaskStatusEnum.DONE,
                TaskPriorityEnum.LOW,
                user1,
                LocalDateTime.now().minusDays(10)
        );
        taskRepository.save(task7);

        Task task8 = new Task(
                "테스트 코드 작성",
                "단위 테스트와 통합 테스트를 작성합니다",
                TaskStatusEnum.TODO,
                TaskPriorityEnum.LOW,
                user3,
                LocalDateTime.now().minusDays(2)
        );
        taskRepository.save(task8);

        log.info("taskRepository.count() = " + taskRepository.count());

        // ===== 5. ActivityLog 생성 (생성자 방식) =====

    }
}
