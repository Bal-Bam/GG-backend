package com.gg.bal_bam.domain.step.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class WeeklyResponse {

    private List<DailyWalkResponse> weeklyData;
    private Integer dailyGoalSteps;

    private WeeklyResponse(List<DailyWalkResponse> weeklyData, Integer dailyGoalSteps) {
        this.weeklyData = weeklyData;
        this.dailyGoalSteps = dailyGoalSteps;
    }

    public static WeeklyResponse of(List<DailyWalkResponse> weeklyData, Integer dailyGoalSteps) {
        return new WeeklyResponse(weeklyData, dailyGoalSteps);
    }

}
