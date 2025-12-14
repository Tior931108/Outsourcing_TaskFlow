package com.example.outsourcing_taskflow.domain.search.model.dto;

import com.example.outsourcing_taskflow.common.entity.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchTaskResultDto {
// - Properties
    private final Long id;
    private final String title;
    private final String description;
    private final String status;

// - Methods
    // - Static Factory Methods
    public static SearchTaskResultDto from(Task task) {
        return new SearchTaskResultDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name());
    }
}
