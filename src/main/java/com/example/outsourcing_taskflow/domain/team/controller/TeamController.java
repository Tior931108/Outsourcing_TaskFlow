package com.example.outsourcing_taskflow.domain.team.controller;

import com.example.outsourcing_taskflow.domain.team.dto.request.CreateTeamRequest;
import com.example.outsourcing_taskflow.domain.team.dto.response.CreateTeamResponse;
import com.example.outsourcing_taskflow.domain.team.dto.response.ReadTeamDetailResponse;
import com.example.outsourcing_taskflow.domain.team.dto.response.TeamListResponseDto;
import com.example.outsourcing_taskflow.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**팀 상세 조회 API
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ReadTeamDetailResponse getTeamDetailApi(@PathVariable("id") Long id) {

        // 핵심 비지니스 로직
        ReadTeamDetailResponse teamDetailResponse = teamService.getTeamDetail(id);

        return teamDetailResponse;
    }


    /**
     * 팀 목록 조회 API
     */
    @GetMapping
    public List<TeamListResponseDto> getTeamListApi() {

        // 핵심 비지니스 로직
        List<TeamListResponseDto> teamResponse = teamService.getTeamList();

        // 응답 반환
        return teamResponse;
    }
}
