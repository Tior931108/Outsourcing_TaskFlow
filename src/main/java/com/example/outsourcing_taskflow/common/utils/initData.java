package com.example.outsourcing_taskflow.common.utils;

import com.example.outsourcing_taskflow.common.entity.*;
import com.example.outsourcing_taskflow.common.enums.TaskPriorityEnum;
import com.example.outsourcing_taskflow.common.enums.TaskStatusEnum;
import com.example.outsourcing_taskflow.domain.activitylog.repository.ActivityLogRepository;
import com.example.outsourcing_taskflow.domain.comment.repository.CommentRepository;
import com.example.outsourcing_taskflow.domain.member.repository.MemberRepository;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.team.repository.TeamRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
public class initData implements ApplicationRunner {

    // 레파지토리 의존성 주입하는 공간
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ActivityLogRepository activityLogRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        log.info("========================================");
        log.info("📦 초기 데이터 생성 시작...");
        log.info("========================================");

        // ===== 1. 사용자 생성 =====

        User user1 = new User("johndoe", "john@example.com",
                passwordEncoder.encode("Password123!"), "John Doe");
        user1.updateAdminRole();
        userRepository.save(user1);

        User user2 = new User("janedoe", "jane@example.com",
                passwordEncoder.encode("Password123!"), "Jane Doe");
        userRepository.save(user2);

        User user3 = new User("leeyounghe", "younghee@example.com",
                passwordEncoder.encode("Password123!"), "이영희");
        userRepository.save(user3);

        log.info("✅ 사용자 3명 생성 완료");

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

        log.info("✅ Team 5개 생성 완료");

        // ===== 3. Member 생성 =====
        memberRepository.save(new Member(team1, user1));
        memberRepository.save(new Member(team3, user1));
        memberRepository.save(new Member(team1, user2));
        memberRepository.save(new Member(team2, user2));
        memberRepository.save(new Member(team4, user2));
        memberRepository.save(new Member(team2, user3));
        memberRepository.save(new Member(team5, user3));

        log.info("✅ Member 7개 생성 완료");

        // ===== 4. Task 생성 (다양한 생성 날짜) =====

        // Task 1 - 6일 전 생성 (토요일)
        Task task1 = new Task(
                "요구사항 분석",
                "프로젝트 요구사항을 분석합니다",
                TaskStatusEnum.DONE,
                TaskPriorityEnum.HIGH,
                user1,
                now.minusDays(4)
        );
        taskRepository.save(task1);
        taskRepository.flush();
        updateTaskDates(task1.getId(), now.minusDays(6), now.minusDays(5));

        // Task 2 - 5일 전 생성 (일요일)
        Task task2 = new Task(
                "데이터베이스 설계",
                "ERD를 작성하고 테이블을 설계합니다",
                TaskStatusEnum.DONE,
                TaskPriorityEnum.HIGH,
                user2,
                now.minusDays(3)
        );
        taskRepository.save(task2);
        taskRepository.flush();
        updateTaskDates(task2.getId(), now.minusDays(5), now.minusDays(4));

        // Task 3 - 4일 전 생성 (월요일)
        Task task3 = new Task(
                "백엔드 API 개발",
                "RESTful API를 구현합니다",
                TaskStatusEnum.IN_PROGRESS,
                TaskPriorityEnum.HIGH,
                user2,
                now.plusDays(3)
        );
        taskRepository.save(task3);
        taskRepository.flush();
        updateTaskDates(task3.getId(), now.minusDays(4), now.minusDays(4));

        // Task 4 - 3일 전 생성 (화요일)
        Task task4 = new Task(
                "프론트엔드 개발",
                "React를 사용하여 사용자 인터페이스를 구현합니다",
                TaskStatusEnum.IN_PROGRESS,
                TaskPriorityEnum.HIGH,
                user1,
                now.plusDays(7)
        );
        taskRepository.save(task4);
        taskRepository.flush();
        updateTaskDates(task4.getId(), now.minusDays(3), now.minusDays(3));

        // Task 5 - 2일 전 생성 (수요일)
        Task task5 = new Task(
                "회원가입 기능 구현",
                "사용자 회원가입 기능을 구현합니다",
                TaskStatusEnum.TODO,
                TaskPriorityEnum.MEDIUM,
                user3,
                now.plusDays(4)
        );
        taskRepository.save(task5);
        taskRepository.flush();
        updateTaskDates(task5.getId(), now.minusDays(2), now.minusDays(2));

        // Task 6 - 2일 전 생성 (수요일)
        Task task6 = new Task(
                "데이터베이스 최적화",
                "쿼리 성능을 개선합니다",
                TaskStatusEnum.TODO,
                TaskPriorityEnum.MEDIUM,
                user3,
                now.plusDays(10)
        );
        taskRepository.save(task6);
        taskRepository.flush();
        updateTaskDates(task6.getId(), now.minusDays(2), now.minusDays(2));

        // Task 7 - 1일 전 생성 (목요일)
        Task task7 = new Task(
                "대시보드 UI 디자인",
                "대시보드를 위한 와이어프레임을 제작합니다",
                TaskStatusEnum.TODO,
                TaskPriorityEnum.LOW,
                user1,
                now.plusDays(5)
        );
        taskRepository.save(task7);
        taskRepository.flush();
        updateTaskDates(task7.getId(), now.minusDays(1), now.minusDays(1));

        // Task 8 - 오늘 생성 (금요일)
        Task task8 = new Task(
                "테스트 코드 작성",
                "단위 테스트와 통합 테스트를 작성합니다",
                TaskStatusEnum.TODO,
                TaskPriorityEnum.LOW,
                user3,
                now.minusDays(2)
        );
        taskRepository.save(task8);

        log.info("✅ Task 8개 생성 완료 (다양한 날짜)");

        // ===== 5. Comment 생성 (일반 댓글 + 대댓글) =====

        // Task 1의 댓글들
        Comment comment1 = new Comment(
                "요구사항 분석 잘 정리되었네요!",
                user2,
                task1
        );
        commentRepository.save(comment1);

        Comment comment2 = new Comment(
                "감사합니다. 추가 의견 있으시면 알려주세요.",
                user1,
                task1,
                comment1  // ✅ 대댓글 (부모: comment1)
        );
        commentRepository.save(comment2);

        // Task 2의 댓글들
        Comment comment3 = new Comment(
                "ERD 설계가 깔끔합니다.",
                user1,
                task2
        );
        commentRepository.save(comment3);

        Comment comment4 = new Comment(
                "User 테이블에 인덱스 추가가 필요할 것 같습니다.",
                user3,
                task2
        );
        commentRepository.save(comment4);

        Comment comment5 = new Comment(
                "좋은 제안입니다. 반영하겠습니다!",
                user2,
                task2,
                comment4  // ✅ 대댓글 (부모: comment4)
        );
        commentRepository.save(comment5);

        // Task 3의 댓글들
        Comment comment6 = new Comment(
                "API 개발 일정이 타이트한데 괜찮을까요?",
                user1,
                task3
        );
        commentRepository.save(comment6);

        Comment comment7 = new Comment(
                "최선을 다해보겠습니다!",
                user2,
                task3,
                comment6  // ✅ 대댓글
        );
        commentRepository.save(comment7);

        Comment comment8 = new Comment(
                "필요하면 도와드릴게요.",
                user3,
                task3,
                comment6  // ✅ 대댓글 (같은 부모)
        );
        commentRepository.save(comment8);

        // Task 4의 댓글들
        Comment comment9 = new Comment(
                "컴포넌트 구조는 어떻게 가져가시나요?",
                user2,
                task4
        );
        commentRepository.save(comment9);

        Comment comment10 = new Comment(
                "Atomic Design 패턴으로 진행하고 있습니다.",
                user1,
                task4,
                comment9  // ✅ 대댓글
        );
        commentRepository.save(comment10);

        // Task 5의 댓글들
        Comment comment11 = new Comment(
                "회원가입 유효성 검사 꼼꼼히 부탁드립니다.",
                user1,
                task5
        );
        commentRepository.save(comment11);

        Comment comment12 = new Comment(
                "이메일 중복 체크도 추가할게요.",
                user3,
                task5,
                comment11  // ✅ 대댓글
        );
        commentRepository.save(comment12);

        // Task 6의 댓글
        Comment comment13 = new Comment(
                "쿼리 최적화 전에 현재 성능 측정 먼저 해주세요.",
                user2,
                task6
        );
        commentRepository.save(comment13);

        // Task 7의 댓글
        Comment comment14 = new Comment(
                "Figma로 먼저 디자인 시안 공유 부탁드립니다.",
                user3,
                task7
        );
        commentRepository.save(comment14);

        // Task 8의 댓글
        Comment comment15 = new Comment(
                "테스트 커버리지 목표는 80% 이상입니다.",
                user1,
                task8
        );
        commentRepository.save(comment15);

        log.info("✅ Comment 15개 생성 완료 (일반 댓글 8개 + 대댓글 7개)");

        // ===== 6. ActivityLog 생성 =====

        log.info("========================================");
        log.info("🎉 초기 데이터 생성 완료!");
        log.info("========================================");
        log.info("📊 생성된 데이터:");
        log.info("   - Users: 3명");
        log.info("   - Teams: 5개");
        log.info("   - Members: 7개");
        log.info("   - Tasks: 8개 (6일 전~오늘)");
        log.info("   - Comments: 15개 (일반 8개 + 대댓글 7개)");
        log.info("   - ActivityLogs: 0개 [로그 AOP 구현 완료되는대로 테스트 진행]");
        log.info("========================================");
    }

    /**
     * Task의 created_at, updated_at을 수정하는 헬퍼 메서드
     */
    private void updateTaskDates(Long taskId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        entityManager.createNativeQuery(
                        "UPDATE tasks SET created_at = :createdAt, updated_at = :updatedAt WHERE id = :id"
                )
                .setParameter("createdAt", createdAt)
                .setParameter("updatedAt", updatedAt)
                .setParameter("id", taskId)
                .executeUpdate();
    }
}
