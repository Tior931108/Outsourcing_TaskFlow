package com.example.outsourcing_taskflow.common.entity;

import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "teams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder  // 테스트 연습
@AllArgsConstructor(access = AccessLevel.PRIVATE)  // Builder를 위해 필요
public class Team extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 팀 PK

    @Column(length = 100, nullable = false, unique = true)
    private String teamName; // 팀 이름

    private String description; // 팀 설명

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private IsDeleted isDeleted = IsDeleted.FALSE; // 삭제 여부

}
