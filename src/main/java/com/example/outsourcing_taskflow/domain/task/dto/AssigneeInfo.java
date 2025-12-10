package com.example.outsourcing_taskflow.domain.task.dto;

import com.example.outsourcing_taskflow.common.entity.User;
import lombok.Getter;

@Getter
public class AssigneeInfo {
    private final Long id;
    private final String username;
    private final String name;

    public AssigneeInfo(User user) {
        this.id = user.getId();
        this.username = user.getUserName();
        this.name = user.getName();
    }
}
