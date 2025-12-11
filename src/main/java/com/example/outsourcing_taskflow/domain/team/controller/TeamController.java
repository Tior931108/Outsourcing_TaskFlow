package com.example.outsourcing_taskflow.domain.team.controller;

import com.example.outsourcing_taskflow.common.response.ApiResponse;
import com.example.outsourcing_taskflow.domain.team.dto.request.CreateTeamMemberRequest;
import com.example.outsourcing_taskflow.domain.team.dto.request.CreateTeamRequest;
import com.example.outsourcing_taskflow.domain.team.dto.request.UpdateTeamRequest;
import com.example.outsourcing_taskflow.domain.team.dto.response.*;
import com.example.outsourcing_taskflow.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<CreateTeamResponse>> createTeamApi(@Valid @RequestBody CreateTeamRequest createTeamRequest) {

        // 핵심 비지니스 로직
        CreateTeamResponse response = teamService.createTeam(createTeamRequest);

        // 응답 반환
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("팀이 생성되었습니다.", response));
    }

    /**
     * 팀 상세 조회 API
     * @param id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamDetailResponse>> getTeamDetailApi(@PathVariable("id") Long id) {

        // 핵심 비지니스 로직
        TeamDetailResponse teamDetailResponse = teamService.getTeamDetail(id);

        // 응답 반환
        return ResponseEntity.ok(
                ApiResponse.success("팀 조회 성공", teamDetailResponse));
    }

    /**
     * 팀 목록 조회 API
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeamListResponse>>> getTeamListApi() {

        // 핵심 비지니스 로직
        List<TeamListResponse> teamResponse = teamService.getTeamList();

        // 응답 반환
        return ResponseEntity.ok(
                ApiResponse.success("팀 목록 조회 성공", teamResponse)
        );
    }

    /**
     * 팀 수정 API
     * @param id
     * @param request
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateTeamResponse>> updateTeamApi(@PathVariable("id") Long id, @Valid @RequestBody UpdateTeamRequest request) {

        // 핵심 비지니스 로직
        UpdateTeamResponse updateTeamResponse = teamService.updateTeam(id, request);

        // 응답 반환
        return ResponseEntity.ok(
                ApiResponse.success("팀 정보가 수정되었습니다.", updateTeamResponse)
        );
    }

    /**
     * 팀 삭제 API
     * @param id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeamApi(@PathVariable("id") Long id) {

        // 핵심 비지니스 로직
        teamService.deleteTeam(id);

        // 응답 반환
        return ResponseEntity.ok(
                ApiResponse.success("팀이 삭제되었습니다.")
        );
    }


    /**
     * 팀 멤버 추가 API
     */
    @PostMapping("/{teamId}/members")
    public ResponseEntity<ApiResponse<CreateTeamMemberResponse>> addTeamMemberApi(@PathVariable Long teamId, @RequestBody CreateTeamMemberRequest request) {

        // 핵심 비지니스 로직
        CreateTeamMemberResponse response = teamService.addTeamMember(teamId, request);

        // 응답 반환
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("팀 멤버가 추가되었습니다.", response));
    }

    /**
     * 팀 멤버 조회 API
     */
    @GetMapping("/{teamId}/members")
    public ResponseEntity<ApiResponse<List<TeamMemberResponse>>> getTeamMemberApi(@PathVariable Long teamId) {

        // 핵심 비지니스 로직
        List<TeamMemberResponse> teamMemberResponse = teamService.getTeamMembers(teamId);

        // 응답 반환
        return ResponseEntity.ok(
                ApiResponse.success("팀 멤버 조회 성공", teamMemberResponse));
    }
}