package com.gg.bal_bam.domain.step.service;

import com.gg.bal_bam.domain.step.StepRepository;
import com.gg.bal_bam.domain.step.dto.DailyResponse;
import com.gg.bal_bam.domain.step.dto.DailyWalkResponse;
import com.gg.bal_bam.domain.step.dto.MonthlyResponse;
import com.gg.bal_bam.domain.step.dto.WeeklyResponse;
import com.gg.bal_bam.domain.step.model.Step;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StepStatsService {

    private final StepRepository stepRepository;
    private final UserRepository userRepository;

    public DailyResponse getDailyStats(UUID userId, LocalDate date) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + userId));

        Step step = stepRepository.findByUserAndDate(user, date)
                .orElseThrow(() -> new CustomException("해당 날짜의 Step 데이터를 찾을 수 없습니다."));

        return DailyResponse.of(
                step.getTotalWalkSteps(),
                step.getTotalWalkTime(),
                15000L //임시 데이터
        );
    }

    public WeeklyResponse getWeeklyStats(UUID userId, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + userId));

        List<Step> steps = stepRepository.findByUserAndDateBetween(user, startDate, endDate);

        List<DailyWalkResponse> dailyWalks = steps.stream()
                .map(step -> DailyWalkResponse.of(step.getTotalWalkSteps(), step.getDate()))
                .toList();

        Integer dailyGoal = user.getDailyGoalSteps();

        return WeeklyResponse.of(dailyWalks, dailyGoal);
    }

    public MonthlyResponse getMonthlyStats(UUID userId, LocalDate thisMonth, LocalDate lastMonth) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + userId));

        LocalDate thisMonthStart = thisMonth.withDayOfMonth(1);
        LocalDate thisMonthEnd = thisMonth;

        LocalDate lastMonthStart = thisMonth.withDayOfMonth(1);
        LocalDate lastMonthEnd = thisMonth.withDayOfMonth(lastMonth.lengthOfMonth());

        int thisMonthGoals = stepRepository.countGoalAchievedDays(user, thisMonthStart, thisMonthEnd);
        int lastMonthGoals = stepRepository.countGoalAchievedDays(user, lastMonthStart, lastMonthEnd);

        int thisMonthDays = thisMonth.getDayOfMonth();
        int lastMonthDays = lastMonth.getDayOfMonth();

        float thisMonthRate = (float) thisMonthGoals / thisMonthDays * 100;
        float lastMonthRate = (float) lastMonthGoals / lastMonthDays * 100;

        return MonthlyResponse.of(thisMonthRate, lastMonthRate);
    }
}
