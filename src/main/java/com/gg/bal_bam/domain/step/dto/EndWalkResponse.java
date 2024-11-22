package com.gg.bal_bam.domain.step.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class EndWalkResponse {

    private Integer totalWalkSteps;
    private Long totalWalkTime;
    private Float goalAchievementRate;

    private EndWalkResponse(Integer totalWalkSteps, Long totalWalkTime, Float goalAchievementRate) {
        this.totalWalkSteps = totalWalkSteps;
        this.totalWalkTime = totalWalkTime;
        this.goalAchievementRate = goalAchievementRate;
    }

    public static EndWalkResponse of(Integer totalWalkSteps, Long totalWalkTime, Float goalAchievementRate) {
        return new EndWalkResponse(totalWalkSteps, totalWalkTime, goalAchievementRate);
    }
}
