package com.example.outsourcing_taskflow.common.entity;

import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder  // 테스트 연습
@AllArgsConstructor(access = AccessLevel.PRIVATE)  // Builder를 위해 필요
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 유저 PK

    @Column(length = 30 ,nullable = false, unique = true)
    private String userName; // 아이디(이름)

    @Column(length = 100 ,nullable = false, unique = true)
    private String email; // 이메일

    @Column(length = 255, nullable = false)
    private String password; // 패스워드

    @Column(length = 30, nullable = false)
    private String name; // 사용자 이름

    @Column(length = 10, nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private UserRoleEnum role = UserRoleEnum.USER; // 유저 권한

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private IsDeleted isDeleted = IsDeleted.FALSE; // 삭제 여부

}
