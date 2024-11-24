package com.gg.bal_bam.domain.step.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MonthlyResponse {

    private Float lastMonthAchievementRate;
    private Float thisMonthAchievementRate;

    private MonthlyResponse(Float lastMonthAchievementRate, Float thisMonthAchievementRate) {
        this.lastMonthAchievementRate = lastMonthAchievementRate;
        this.thisMonthAchievementRate = thisMonthAchievementRate;
    }

    public static MonthlyResponse of(Float lastMonthAchievementRate, Float thisMonthAchievementRate) {
        return new MonthlyResponse(lastMonthAchievementRate, thisMonthAchievementRate);
    }
}
