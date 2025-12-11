package com.example.outsourcing_taskflow.domain.search.model.response;

import com.example.outsourcing_taskflow.domain.search.model.dto.SearchTaskResultDto;
import com.example.outsourcing_taskflow.domain.search.model.dto.SearchTeamResultDto;
import com.example.outsourcing_taskflow.domain.search.model.dto.SearchUserResultDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetSearchResponse {
// - Properties
    private final List<SearchTaskResultDto> tasks;
    private final List<SearchUserResultDto> users;
    private final List<SearchTeamResultDto> teams;
}
