package com.example.outsourcing_taskflow.domain.search.model.dto;

import com.example.outsourcing_taskflow.common.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchUserResultDto {
// - Properties
    private final Long id;
    private final String name;
    private final String description;

// - Methods
    // - Static Factory Methods
    public static SearchUserResultDto from(User user) {
        return new SearchUserResultDto(
                user.getId(),
                user.getName(),
                user.getUserName());
    }
}
