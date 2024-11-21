package com.gg.bal_bam.domain.step.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DailyWalkResponse {

    private Integer totalWalkSteps;
    private LocalDate date;

    private DailyWalkResponse(Integer totalWalkSteps, LocalDate date) {
        this.totalWalkSteps = totalWalkSteps;
        this.date = date;
    }

    public static DailyWalkResponse of(Integer totalWalkSteps, LocalDate date) {
        return new DailyWalkResponse(totalWalkSteps, date);
    }
}
