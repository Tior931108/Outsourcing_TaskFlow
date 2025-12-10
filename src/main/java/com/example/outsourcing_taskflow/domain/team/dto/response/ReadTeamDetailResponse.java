package com.example.outsourcing_taskflow.domain.team.dto.response;

import com.example.outsourcing_taskflow.common.entity.Member;
import com.example.outsourcing_taskflow.common.entity.Team;
import com.example.outsourcing_taskflow.domain.member.dto.response.ReadMemberDetailReasponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReadTeamDetailResponse {

    // 속성
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<ReadMemberDetailReasponse> members;

    // 생성자
    public ReadTeamDetailResponse(Team team, List<ReadMemberDetailReasponse> members) {
        this.id = team.getId();
        this.name = team.getTeamName();
        this.description = team.getDescription();
        this.createdAt = team.getCreatedAt();
        this.members = members;
    }
}
