package com.example.outsourcing_taskflow.domain.team.controller;

import com.example.outsourcing_taskflow.common.config.security.auth.AuthUserDto;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    // 팀 생성 API
    @PostMapping
    public ResponseEntity<ApiResponse<CreateTeamResponse>> createTeamApi(
            @Valid @RequestBody CreateTeamRequest createTeamRequest) {

        CreateTeamResponse response = teamService.createTeam(createTeamRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("팀이 생성되었습니다.", response));
    }

    // 팀 상세 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamDetailResponse>> getTeamDetailApi(
            @PathVariable("id") Long id) {

        TeamDetailResponse teamDetailResponse = teamService.getTeamDetail(id);

        return ResponseEntity.ok(
                ApiResponse.success("팀 조회 성공", teamDetailResponse));
    }

    // 팀 목록 조회 API
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeamListResponse>>> getTeamListApi() {

        List<TeamListResponse> teamResponse = teamService.getTeamList();

        return ResponseEntity.ok(
                ApiResponse.success("팀 목록 조회 성공", teamResponse));
    }

    // 팀 수정 API
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateTeamResponse>> updateTeamApi(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateTeamRequest request,
            @AuthenticationPrincipal AuthUserDto authUserDto) {

        UpdateTeamResponse updateTeamResponse = teamService.updateTeam(id, request, authUserDto);

        return ResponseEntity.ok(
                ApiResponse.success("팀 정보가 수정되었습니다.", updateTeamResponse));
    }

    // 팀 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeamApi(
            @PathVariable("id") Long id) {

        teamService.deleteTeam(id);

        return ResponseEntity.ok(
                ApiResponse.success("팀이 삭제되었습니다."));
    }

     // 팀 멤버 추가 API
    @PostMapping("/{teamId}/members")
    public ResponseEntity<ApiResponse<CreateTeamMemberResponse>> addTeamMemberApi(
            @PathVariable Long teamId,
            @RequestBody CreateTeamMemberRequest request) {

        CreateTeamMemberResponse response = teamService.addTeamMember(teamId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("팀 멤버가 추가되었습니다.", response));
    }

    // 팀 멤버 조회 API
    @GetMapping("/{teamId}/members")
    public ResponseEntity<ApiResponse<List<TeamMemberResponseDto>>> getTeamMemberApi(
            @PathVariable Long teamId) {

        List<TeamMemberResponseDto> teamMemberResponseDto = teamService.getTeamMembers(teamId);

        return ResponseEntity.ok(
                ApiResponse.success("팀 멤버 조회 성공", teamMemberResponseDto));
    }

    // 팀 멤버 제거 API
    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteTeamMemberApi(
            @PathVariable Long teamId,
            @PathVariable Long userId,
            @AuthenticationPrincipal AuthUserDto authUserDto) {

        teamService.deleteTeamMember(teamId, userId, authUserDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 멤버가 제거되었습니다.", null));
    }
}