package com.gg.bal_bam.domain.step.service;

import com.gg.bal_bam.domain.step.StepRepository;
import com.gg.bal_bam.domain.step.model.Step;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StepService {

    private final StepRepository stepRepository;
    private final UserRepository userRepository;

    @Transactional
    public void startWalk(UUID userId, LocalDateTime startTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + userId));

        //오늘의 Step 데이터 조회
        LocalDate today = LocalDate.now();
        Step step = stepRepository.findByUserAndDate(user, today)
                .orElseGet(() -> Step.createStep(user, today)); // 없으면 생성

        step.startWalking(startTime);

    }


    @Transactional
    public void endWalk(UUID userId, LocalDateTime endTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + userId));

        Step step = stepRepository.findByUserAndDate(user, LocalDate.now())
                .orElseThrow(() -> new CustomException("오늘의 Step 데이터를 찾을 수 없습니다."));

        Long walkTime = step.calculateWalkTime(endTime); // 산책 시간 계산
        step.endWalking();

        // 산책 시간 계산
        Integer walkSteps = 10000; // 걸음 수 계산 필요

        Float goalAchievementRate = calculateGoalAchievementRate(
                step.getTotalWalkSteps() + walkSteps, user.getDailyGoalSteps()
        );


        step.updateStep(
                step.getTotalWalkSteps() + walkSteps,
                step.getTotalWalkTime() + walkTime,
                goalAchievementRate
        );

    }

    // 목표 달성률 계산 메서드
    private Float calculateGoalAchievementRate(Integer totalWalkSteps, Integer goal) {
        if (goal == null || goal == 0) {
            return 0.0f;
        }

        return (float) totalWalkSteps / goal * 100;
    }
}
