package com.example.outsourcing_taskflow.domain.team.dto.response;

import com.example.outsourcing_taskflow.common.entity.Team;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonPropertyOrder({"id", "name", "description", "createdAt", "members"})
public class CreateTeamMemberResponse {

    // 속성
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<TeamMemberResponseDto> members;

    // 생성자
    public CreateTeamMemberResponse(Team team, List<TeamMemberResponseDto> members) {
        this.id = team.getId();
        this.name = team.getTeamName();
        this.description = team.getDescription();
        this.createdAt = team.getCreatedAt();
        this.members = members;
    }

}
