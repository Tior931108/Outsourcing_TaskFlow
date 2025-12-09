package com.example.outsourcing_taskflow.domain.team.service;

import com.example.outsourcing_taskflow.common.entity.Team;
import com.example.outsourcing_taskflow.domain.team.dto.request.CreateTeamRequest;
import com.example.outsourcing_taskflow.domain.team.dto.response.CreateTeamResponse;
import com.example.outsourcing_taskflow.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    /**
     * 팀 생성
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
}
