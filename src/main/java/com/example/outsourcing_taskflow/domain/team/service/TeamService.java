package com.example.outsourcing_taskflow.domain.team.service;

import com.example.outsourcing_taskflow.common.config.security.auth.AuthUserDto;
import com.example.outsourcing_taskflow.common.entity.Member;
import com.example.outsourcing_taskflow.common.entity.Team;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.ErrorMessage;
import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import com.example.outsourcing_taskflow.domain.member.dto.response.MemberListResponseDto;
import com.example.outsourcing_taskflow.domain.member.dto.response.MemberDetailReasponseDto;
import com.example.outsourcing_taskflow.domain.member.repository.MemberRepository;
import com.example.outsourcing_taskflow.domain.team.dto.request.CreateTeamMemberRequest;
import com.example.outsourcing_taskflow.domain.team.dto.request.CreateTeamRequest;
import com.example.outsourcing_taskflow.domain.team.dto.request.UpdateTeamRequest;
import com.example.outsourcing_taskflow.domain.team.dto.response.*;
import com.example.outsourcing_taskflow.domain.team.repository.TeamRepository;
import com.example.outsourcing_taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.outsourcing_taskflow.common.enums.ErrorMessage.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    // 팀 생성
    public CreateTeamResponse createTeam(CreateTeamRequest createTeamRequest) {

        String name = createTeamRequest.getName();
        String description = createTeamRequest.getDescription();

        // 팀 이름 중복 확인
        teamRepository.findByTeamNameAndIsDeletedFalse(name)
                .ifPresent(team -> {
                    throw new CustomException(EXIST_TEAM_NAME);});

        Team newTeam = new Team(name, description);

        Team savedTeam = teamRepository.save(newTeam);

        CreateTeamResponse response = new CreateTeamResponse(savedTeam);

        return response;
    }

    // 팀 상세 조회
    @Transactional(readOnly = true)
    public TeamDetailResponse getTeamDetail(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_TEAM));

        List<Member> members = memberRepository.findAllByTeamIdWithUser(teamId);
        log.info("조회된 멤버 수: {}", members.size());


        List<MemberDetailReasponseDto> memberResponses = new ArrayList<>();

        for (Member member : members) {
            MemberDetailReasponseDto memberResponse = new MemberDetailReasponseDto(member.getUser());
            memberResponses.add(memberResponse);
        }

        TeamDetailResponse teamDetailResponse = new TeamDetailResponse(team, memberResponses);

        return teamDetailResponse;
    }


    // 팀 목록 조회
    @Transactional(readOnly = true)
    public List<TeamListResponse> getTeamList() {

        List<Team> teams = teamRepository.findAll();

        List<Member> allMembersWithUsers = memberRepository.findAllWithUser();

        // 인 메모리 맵
        Map<Long, List<Member>> membersByTeamId = allMembersWithUsers.stream()
                .collect(Collectors.groupingBy(member -> member.getTeam().getId()));

        List<TeamListResponse> teamResponseDtos = new ArrayList<>();

        for (Team team : teams) {
            List<Member> members = membersByTeamId.getOrDefault(team.getId(), Collections.emptyList());

            List<MemberListResponseDto> memberDtos = members.stream()
                    .map(member -> new MemberListResponseDto(member.getUser()))
                    .collect(Collectors.toList());

            TeamListResponse teamResponseDto = new TeamListResponse(team, memberDtos);
            teamResponseDtos.add(teamResponseDto);
        }

        return teamResponseDtos;
    }

    // 팀 수정
    public UpdateTeamResponse updateTeam(Long teamId, UpdateTeamRequest request, AuthUserDto authUserDto) {

        // 권한 검증
//        if (!UserRoleEnum.ADMIN.equals(authUserDto.getRole())) {
//            throw new CustomException(NOT_MODIFY_AUTHORIZED);
//        }

        // [현재 접속중인 사용자] = ![작업 담당자] && ![관리자] -> 403 Forbidden: 수정 권한 없음
        // authUserDto에 현재 접속중인 사용자 id, username, role이 담겨있음

        // 권한 검증
        boolean isAdmin = authUserDto.getRole().equals(UserRoleEnum.ADMIN.getRole()); // JWT로부터 role을 String 권한으로 주입할 때 "ROLE_ADMIN" 형태인지 확인
        if (!isAdmin) {
            throw new CustomException(ErrorMessage.NOT_MODIFY_AUTHORIZED);
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(()-> new CustomException(NOT_FOUND_TEAM));

        team.update(request.getName(), request.getDescription());

        List<Member> members = memberRepository.findAllByTeamIdWithUser(teamId);

        List<MemberDetailReasponseDto> memberResponses = new ArrayList<>();

        for (Member member : members) {
            MemberDetailReasponseDto memberResponse = new MemberDetailReasponseDto(member.getUser());
            memberResponses.add(memberResponse);
        }

        UpdateTeamResponse updateTeamResponse = new UpdateTeamResponse(team, memberResponses);

        return updateTeamResponse;
    }

    // 팀 삭제
    public void deleteTeam(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(()-> new CustomException(NOT_FOUND_TEAM));

        // 팀에 멤버가 존재하면 삭제할 수 없음
        boolean hasMembers= memberRepository.existsByTeamId(teamId);

        if (hasMembers) {
            throw new CustomException(EXIST_TEAM_MEMBER_NOT_DELETE);
        }

        team.softDelete();
    }

    // 팀 멤버 추가
    public CreateTeamMemberResponse addTeamMember(Long teamId, CreateTeamMemberRequest request) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_TEAM));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        // 이미 가입된 멤버인지 확인
        boolean exists = memberRepository.existsByTeamAndUser(team, user);
        if (exists) {
            throw new CustomException(EXIST_TEAM_MEMBER);
        }

        // 팀 + 유저 = 멤버에 저장
        Member newMember = new Member(team, user);
        memberRepository.save(newMember);

        // 현재 팀 멤버 전체 조회
        List<Member> updatedMembers = memberRepository.findAllByTeamIdWithUser(teamId);

        List<TeamMemberResponseDto> memberResponses = new ArrayList<>();

        for (Member member : updatedMembers) {
            TeamMemberResponseDto response = new TeamMemberResponseDto(member.getUser());
            memberResponses.add(response);
        }

        return new CreateTeamMemberResponse(team, memberResponses);
    }

    // 팀 멤버 조회
    @Transactional(readOnly = true)
    public List<TeamMemberResponseDto> getTeamMembers(Long teamId) {

        boolean exists = teamRepository.existsById(teamId);

        if (!exists) {
            throw new CustomException(NOT_FOUND_TEAM);
        }

        List<Member> members = memberRepository.findAllByTeamIdWithUser(teamId);

        List<TeamMemberResponseDto> memberResponses = new ArrayList<>();

        for (Member member : members) {

            // 탈퇴한 멤버 제외하고, 팀의 멤버 조회
            if (member.getUser().getIsDeleted().equals(IsDeleted.FALSE)) {
                TeamMemberResponseDto memberResponse = new TeamMemberResponseDto(member.getUser());
                memberResponses.add(memberResponse);
            }
        }

        return memberResponses;
    }

    // 팀 멤버 삭제
    public void deleteTeamMember(Long teamId, Long userId, AuthUserDto authUserDto) {

        // 권한 검증
//        if (!UserRoleEnum.ADMIN.equals(authUserDto.getRole())) {
//            throw new CustomException(NOT_REMOVE_AUTHORIZED);
//        }
        // 권한 검증
        boolean isAdmin = authUserDto.getRole().equals(UserRoleEnum.ADMIN.getRole()); // JWT로부터 role을 String 권한으로 주입할 때 "ROLE_ADMIN" 형태인지 확인
        if (!isAdmin) {
            throw new CustomException(ErrorMessage.NOT_REMOVE_AUTHORIZED);
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_TEAM));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        Member member = memberRepository.findByTeamAndUser(team, user)
                .orElseThrow(() -> new CustomException(NOT_FOUND_TEAM_MEMBER));

        memberRepository.delete(member);
    }
}