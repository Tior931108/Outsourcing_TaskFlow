package com.example.outsourcing_taskflow.domain.team.service;

import com.example.outsourcing_taskflow.common.entity.Member;
import com.example.outsourcing_taskflow.common.entity.Team;
import com.example.outsourcing_taskflow.common.exception.CustomException;
import com.example.outsourcing_taskflow.domain.member.dto.response.MemberListResponse;
import com.example.outsourcing_taskflow.domain.member.dto.response.MemberDetailReasponse;
import com.example.outsourcing_taskflow.domain.member.repository.MemberRepository;
import com.example.outsourcing_taskflow.domain.team.dto.request.CreateTeamRequest;
import com.example.outsourcing_taskflow.domain.team.dto.request.UpdateTeamRequest;
import com.example.outsourcing_taskflow.domain.team.dto.response.*;
import com.example.outsourcing_taskflow.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.outsourcing_taskflow.common.enums.ErrorMessage.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    /**
     * 팀 생성
     * @param createTeamRequest
     */
    public CreateTeamResponse createTeam(CreateTeamRequest createTeamRequest) {

        // 1. 데이터 준비
        String name = createTeamRequest.getName();
        String description = createTeamRequest.getDescription();

        // 2. 팀 이름 중복 확인
        teamRepository.findByTeamName(name)
                .ifPresent(team -> {
                    throw new CustomException(EXIST_TEAM_NAME);
                });

        // 3. 팀 엔티티 생성
        Team newTeam = new Team(name, description);

        // 4. 레포지터리에 저장
        Team savedTeam = teamRepository.save(newTeam);

        // 5. dto 생성
        CreateTeamResponse response = new CreateTeamResponse(savedTeam);

        return response;
    }

    /**
     * 팀 상세 조회
     * @param teamId
     */
    public TeamDetailResponse getTeamDetail(Long teamId) {

        // 1. Team 엔티티 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_TEAM));

        // 2. MemberRepository를 사용하여 해당 팀의 모든 멤버 정보와 User 정보를 조회
        List<Member> members = memberRepository.findAllByTeamId(teamId);

        // 3. 멤버 DTO를 담을 빈 리스트를 준비
        List<MemberDetailReasponse> memberResponses = new ArrayList<>();

        // 4. 멤버 엔티티 리스트를 순회하는 for 루프 시작
        for (Member member : members) {

            // 4-1. MemberDetailReasponse로 변환
            MemberDetailReasponse memberResponse = new MemberDetailReasponse(member.getUser());
            // 4-2. 변환된 DTO를 memberResponses 리스트에 추가
            memberResponses.add(memberResponse);
        }

        // 5. 최종 TeamDetailResponse를 생성
        TeamDetailResponse teamDetailResponse = new TeamDetailResponse(team, memberResponses);

        // 6. DTO로 변환하여 반환
        return teamDetailResponse;
    }


    /**
     * 팀 전체 조회
     */
    @Transactional(readOnly = true)
    public List<TeamListResponse> getTeamList() {

        // 1. 데이터 조회: Team 엔티티만 조회 (멤버 정보는 제외)
        List<Team> teams = teamRepository.findAll();

        // 2. 최종 결과를 담을 빈 리스트를 준비
        List<TeamListResponse> teamResponseDtos = new ArrayList<>();

        // 3. 조회된 'teams' 리스트를 순회하는 외부 for 루프를 시작
        for (Team team : teams) {

            // 4. MemberRepository를 사용하여 현재 팀의 ID로 멤버 정보를 별도 조회
            List<Member> members = memberRepository.findAllByTeamId(team.getId());

            // 5. 멤버 DTO를 담을 빈 리스트를 준비
            List<MemberListResponse> memberDtos = new ArrayList<>();

            // 6. 멤버 엔티티 리스트를 순회하는 내부 for 루프
            for (Member member : members) {

                // 6-1. 개별 Member 엔티티를 MemberListDto로 변환
                MemberListResponse memberDto = new MemberListResponse(member.getUser());

                // 6-2. 변환된 DTO를 memberDtos 리스트에 추가
                memberDtos.add(memberDto);
            }

            // 7. 최종 TeamListResponseDto를 생성
            TeamListResponse teamResponseDto = new TeamListResponse(team, memberDtos);

            // 8. 생성된 팀 DTO를 최종 결과 리스트에 추가
            teamResponseDtos.add(teamResponseDto);
        }

        // 9. 최종 변환된 DTO 리스트를 반환
        return teamResponseDtos;
    }

    /**
     * 팀 수정
     * @param teamId
     * @param request
     */
    public UpdateTeamResponse updateTeam(Long teamId, UpdateTeamRequest request) {

        // 1. Team 엔티티 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(()-> new CustomException(NOT_FOUND_TEAM));

        // 2. Team 수정 정보 업데이트
        team.update(request.getName(), request.getDescription());

        // 3. MemberRepository를 사용하여 해당 팀의 모든 멤버 정보와 User 정보를 조회
        List<Member> members = memberRepository.findAllByTeamId(teamId);

        // 4. 멤버 DTO를 담을 빈 리스트를 준비
        List<MemberDetailReasponse> memberResponses = new ArrayList<>();

        // 5. 멤버 엔티티 리스트를 순회하는 for 루프 시작
        for (Member member : members) {

            // 5-1. MemberDetailReasponse로 변환
            MemberDetailReasponse memberResponse = new MemberDetailReasponse(member.getUser());
            // 5-2. 변환된 DTO를 memberResponses 리스트에 추가
            memberResponses.add(memberResponse);
        }

        // 6. 최종 UpdateTeamResponse 생성
        UpdateTeamResponse updateTeamResponse = new UpdateTeamResponse(team, memberResponses);

        // 7. DTO로 변환하여 반환
        return updateTeamResponse;
    }

    /**
     * 팀 삭제
     * @param teamId
     */
    public void deleteTeam(Long teamId) {

        // 1. Team 엔티티 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(()-> new CustomException(NOT_FOUND_TEAM));

        // 2. 팀에 멤버가 존재하면 삭제할 수 없는 로직 구현
        boolean hasMembers= memberRepository.existsByTeamId(teamId);

        if (hasMembers) {
            throw new CustomException(EXIST_TEAM_MEMBER_NOT_DELETE);
        }

        // 3. Team 삭제
        teamRepository.delete(team);
    }

    /**
     * 팀 멤버 조회
     */
    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getTeamMembers(Long teamId) {

        // 1. Team 존재 여부만 확인
        boolean teamExists = teamRepository.existsById(teamId);

        if (!teamExists) {
            throw new CustomException(NOT_FOUND_TEAM);
        }

        // 2. MemberRepository를 사용하여 해당 팀의 모든 멤버 정보와 User 정보를 조회
        List<Member> members = memberRepository.findAllByTeamId(teamId);

        // 3. 최종 결과를 담을 빈 리스트를 준비
        List<TeamMemberResponse> memberResponses = new ArrayList<>();

        // 4. 멤버 엔티티 리스트를 순회하는 for 루프 시작
        for (Member member : members) {

            // 4-1. User 정보를 TeamMemberResponse DTO로 변환
            TeamMemberResponse memberResponse = new TeamMemberResponse(member.getUser());

            // 4-2. 변환된 DTO를 결과 리스트에 추가
            memberResponses.add(memberResponse);
        }

        // 5. 최종 변환된 DTO 리스트를 반환
        return memberResponses;
    }
}