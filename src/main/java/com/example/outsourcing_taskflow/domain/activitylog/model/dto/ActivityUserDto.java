package com.example.outsourcing_taskflow.domain.activitylog.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ActivityUserDto {
// - Properties
    private final Long id;
    private final String username;
    private final String name;
}
