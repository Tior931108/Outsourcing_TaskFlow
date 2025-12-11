package com.example.outsourcing_taskflow.domain.team.dto.response;

import com.example.outsourcing_taskflow.common.entity.Team;
import com.example.outsourcing_taskflow.domain.member.dto.response.MemberDetailReasponse;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonPropertyOrder({"id", "name", "description", "createdAt", "members"})
public class CreateTeamMemberResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<TeamMemberResponse> members;

    public CreateTeamMemberResponse(Team team, List<TeamMemberResponse> members) {
        this.id = team.getId();
        this.name = team.getTeamName();
        this.description = team.getDescription();
        this.createdAt = team.getCreatedAt();
        this.members = members;
    }

}
