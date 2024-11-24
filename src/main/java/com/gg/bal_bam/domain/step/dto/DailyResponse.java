package com.gg.bal_bam.domain.step.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DailyResponse {

    private Integer totalWalkSteps;
    private Long totalWalkTime;
    private Long totalSteps;

    private DailyResponse(Integer totalWalkSteps, Long totalWalkTime, Long totalSteps) {
        this.totalWalkSteps = totalWalkSteps;
        this.totalWalkTime = totalWalkTime;
        this.totalSteps = totalSteps;
    }

    public static DailyResponse of(Integer totalWalkSteps, Long totalWalkTime, Long totalSteps) {
        return new DailyResponse(totalWalkSteps, totalWalkTime, totalSteps);
    }
}
