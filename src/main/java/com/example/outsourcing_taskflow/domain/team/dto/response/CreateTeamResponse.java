package com.example.outsourcing_taskflow.domain.team.dto.response;

import com.example.outsourcing_taskflow.common.entity.Member;
import com.example.outsourcing_taskflow.common.entity.Team;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateTeamResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<Member> members;

    public CreateTeamResponse(Team team) {
        this.id = team.getId();
        this.name = team.getTeamName();
        this.description = team.getDescription();
        this.createdAt = team.getCreatedAt();
        this.members = new ArrayList<>();
    }
}
