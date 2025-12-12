package com.example.outsourcing_taskflow.domain.dashboard.model.dto;

import lombok.Getter;

@Getter
public class DailyTrendDto {

    private String name;       // 요일 (월, 화, 수...)
    private Integer tasks;     // 해당 날짜 작업 수
    private Integer completed; // 완료된 작업 수
    private String date;       // 날짜 (YYYY-MM-DD)

    public DailyTrendDto(String name, Integer tasks, Integer completed, String date) {
        this.name = name;
        this.tasks = tasks;
        this.completed = completed;
        this.date = date;
    }
}
