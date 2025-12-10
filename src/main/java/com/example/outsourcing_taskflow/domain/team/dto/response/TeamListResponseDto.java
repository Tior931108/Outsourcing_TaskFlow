package com.example.outsourcing_taskflow.domain.team.dto.response;

import com.example.outsourcing_taskflow.domain.member.dto.response.MemberListDto;
import com.example.outsourcing_taskflow.domain.member.dto.response.ReadMemberDetailReasponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TeamListResponseDto {

    // 속성
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<MemberListDto> members;

    // 생성자
    public TeamListResponseDto(Long id, String name, String description, LocalDateTime createdAt, List<MemberListDto> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.members = members;
    }
}
