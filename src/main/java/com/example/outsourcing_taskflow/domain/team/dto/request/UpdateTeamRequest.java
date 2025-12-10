package com.example.outsourcing_taskflow.domain.team.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateTeamRequest {

    @NotBlank(message = "팀 이름은 필수입니다.")
    @Size(min = 1, max = 100)
    private String name;

    @Size(max = 255)
    private String description;
}
