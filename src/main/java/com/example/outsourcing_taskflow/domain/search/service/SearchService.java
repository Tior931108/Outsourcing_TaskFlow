package com.example.outsourcing_taskflow.domain.search.service;

import com.example.outsourcing_taskflow.common.annotaion.MeasureAllMethods;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.domain.search.model.dto.SearchTaskResultDto;
import com.example.outsourcing_taskflow.domain.search.model.dto.SearchTeamResultDto;
import com.example.outsourcing_taskflow.domain.search.model.dto.SearchUserResultDto;
import com.example.outsourcing_taskflow.domain.search.model.response.GetSearchResponse;
import com.example.outsourcing_taskflow.domain.task.repository.TaskRepository;
import com.example.outsourcing_taskflow.domain.team.repository.TeamRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@MeasureAllMethods
public class SearchService {
// - Properties
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

// - Methods
    @Transactional(readOnly = true)
    public GetSearchResponse search(String keyword) {
        // - Search Task
        List<SearchTaskResultDto> taskResults = taskRepository
                .searchByKeyword(keyword, IsDeleted.FALSE)
                .stream()
                .map(SearchTaskResultDto::from)
                .toList();
        // - Search User
        List<SearchUserResultDto> userResults = userRepository
                .searchByKeyword(keyword, IsDeleted.FALSE)
                .stream()
                .map(SearchUserResultDto::from)
                .toList();
        // - Search Team
        List<SearchTeamResultDto> teamResults = teamRepository
                .searchByKeyword(keyword)
                .stream()
                .map(SearchTeamResultDto::from)
                .toList();

        // - Return Result
        return new GetSearchResponse(taskResults, userResults, teamResults);
    }
}
