package com.example.outsourcing_taskflow.domain.search.model.dto;

import com.example.outsourcing_taskflow.common.entity.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchTeamResultDto {
// - Properties
    private final Long id;
    private final String name;
    private final String description;

// - Methods
    // - Static Factory Methods
    public static SearchTeamResultDto from(Team team) {
        return new SearchTeamResultDto(
                team.getId(),
                team.getTeamName(),
                team.getDescription());
    }
}
