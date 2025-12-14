package com.example.outsourcing_taskflow.domain.team.model.response;

import com.example.outsourcing_taskflow.common.entity.Team;
import com.example.outsourcing_taskflow.domain.member.model.response.MemberListResponseDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonPropertyOrder({"id", "name", "description", "createdAt", "members"})
public class TeamListResponse {

    // 속성
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<MemberListResponseDto> members;

    // 생성자
    public TeamListResponse(Team team, List<MemberListResponseDto> members) {
        this.id = team.getId();
        this.name = team.getTeamName();
        this.description = team.getDescription();
        this.createdAt = team.getCreatedAt();
        this.members = members;
    }
}
