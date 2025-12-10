package com.example.outsourcing_taskflow.domain.member.dto.response;

import com.example.outsourcing_taskflow.common.entity.Member;
import com.example.outsourcing_taskflow.common.entity.User;
import com.example.outsourcing_taskflow.common.enums.UserRoleEnum;
import lombok.Getter;

@Getter
public class ReadMemberDetailReasponse {

    private Long id;
    private String username;
    private String name;
    private String email;
    private UserRoleEnum role;

    public ReadMemberDetailReasponse(User user) {
        this.id = user.getId();
        this.username = user.getUserName(); // User 엔티티의 필드명에 맞춤
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
