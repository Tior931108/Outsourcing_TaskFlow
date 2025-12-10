package com.example.outsourcing_taskflow.domain.team.service;

import com.example.outsourcing_taskflow.common.entity.Member;
import com.example.outsourcing_taskflow.common.entity.Team;
import com.example.outsourcing_taskflow.common.enums.IsDeleted;
import com.example.outsourcing_taskflow.domain.member.dto.response.MemberListDto;
import com.example.outsourcing_taskflow.domain.member.dto.response.ReadMemberDetailReasponse;
import com.example.outsourcing_taskflow.domain.member.repository.MemberRepository;
import com.example.outsourcing_taskflow.domain.team.dto.request.CreateTeamRequest;
import com.example.outsourcing_taskflow.domain.team.dto.response.CreateTeamResponse;
import com.example.outsourcing_taskflow.domain.team.dto.response.ReadTeamDetailResponse;
import com.example.outsourcing_taskflow.domain.team.dto.response.ReadTeamListResponse;
import com.example.outsourcing_taskflow.domain.team.dto.response.TeamListResponseDto;
import com.example.outsourcing_taskflow.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    /**
     * 팀 생성
     *
     * @param createTeamRequest
     * @return
     */
    public CreateTeamResponse createTeam(CreateTeamRequest createTeamRequest) {

        // 1. 데이터 준비
        String name = createTeamRequest.getName();
        String description = createTeamRequest.getDescription();

        // 2. 팀 이름 중복 확인
        teamRepository.findByTeamName(name)
                .ifPresent(team -> {
                    throw new RuntimeException("이미 존재하는 팀 이름입니다.");     // 에러수정하기
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
     *
     * @param teamId
     */
    public ReadTeamDetailResponse getTeamDetail(Long teamId) {

        // 1. Team 엔티티 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));  // 에러 수정

        // 2. MemberRepository를 사용하여 해당 팀의 모든 멤버 정보와 User 정보를 조회
        List<Member> members = memberRepository.findAllWithUserByTeamId(teamId);

        // 3. dto 생성
        List<ReadMemberDetailReasponse> memberResponses = members.stream()
                .map(Member::getUser)
                .map(ReadMemberDetailReasponse::new)
                .collect(Collectors.toList());

        // dto로 변환하여 반환
        return new ReadTeamDetailResponse(team, memberResponses);
    }


    /**
     * 팀 전체 조회
     */
    @Transactional(readOnly = true)
    public List<TeamListResponseDto> getTeamList() {

        // 1. 데이터 조회: Team 엔티티만 조회합니다. (멤버 정보는 제외)
        List<Team> teams = teamRepository.findAllByIsDeleted(IsDeleted.FALSE);

        // 2. 최종 결과를 담을 빈 리스트를 준비
        List<TeamListResponseDto> teamResponseDtos = new ArrayList<>();

        // 3. 조회된 'teams' 리스트를 순회하는 외부 for 루프를 시작합니다.
        for (Team team : teams) {

            // 4. [핵심] MemberRepository를 사용하여 현재 팀의 ID로 멤버 정보를 별도 조회합니다.
            //    (※ findAllByTeamId는 MemberRepository에 정의되어 있어야 합니다.)
            List<Member> members = memberRepository.findAllByTeamId(team.getId());

            // 5. 멤버 DTO를 담을 빈 리스트를 준비합니다.
            List<MemberListDto> memberDtos = new ArrayList<>();

            // 6. 멤버 엔티티 리스트를 순회하는 내부 for 루프를 시작합니다.
            for (Member member : members) {

                // 6-1. 개별 Member 엔티티를 MemberListDto로 변환합니다.
                MemberListDto memberDto = new MemberListDto(
                        member.getId(),
                        member.getUser().getUserName(),
                        member.getUser().getName(),
                        member.getUser().getEmail(),
                        member.getUser().getRole().toString(),
                        member.getUser().getCreatedAt()
                );

                // 6-2. 변환된 DTO를 memberDtos 리스트에 추가합니다.
                memberDtos.add(memberDto);
            }

            // 7. 최종 TeamListResponseDto를 생성합니다.
            TeamListResponseDto teamResponseDto = new TeamListResponseDto(
                    team.getId(),
                    team.getTeamName(),
                    team.getDescription(),
                    team.getCreatedAt(),
                    memberDtos
            );

            // 9. 생성된 팀 DTO를 최종 결과 리스트에 추가합니다.
            teamResponseDtos.add(teamResponseDto);
        }

        // 10. 최종 변환된 DTO 리스트를 반환합니다.
        return teamResponseDtos;
    }
}
