package com.example.outsourcing_taskflow.domain.team.controller;

import com.example.outsourcing_taskflow.domain.team.dto.request.CreateTeamRequest;
import com.example.outsourcing_taskflow.domain.team.dto.response.CreateTeamResponse;
import com.example.outsourcing_taskflow.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    /**
     * 팀 생성 API
     * @param createTeamRequest
     */
    @PostMapping
    public CreateTeamResponse createTeamApi(@Valid @RequestBody CreateTeamRequest createTeamRequest) {

        // 핵심 비지니스 로직
        CreateTeamResponse response = teamService.createTeam(createTeamRequest);

        // 응답 반환
        return response;
    }
}
