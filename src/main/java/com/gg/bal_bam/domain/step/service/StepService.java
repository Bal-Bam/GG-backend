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

        step.startWalking();

        // 캐싱으로 시작 시간 저장 구현 필요
    }

    @Transactional
    public void endWalk(UUID userId, LocalDateTime endTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + userId));

        //캐싱을 통해 시작시간 확인해서 startTime이 존재하는지 확인하는 로직 필요
        LocalDateTime startTime = null;
//        if (startTime == null) {
//            throw new CustomException("산책 시작 시간이 존재하지 않습니다.");
//        }

        //오늘의 Step 데이터 조회
        LocalDate today = LocalDate.now();
        Step step = stepRepository.findByUserAndDate(user, today)
                .orElseThrow(() -> new CustomException("오늘의 Step 데이터를 찾을 수 없습니다."));

        //산책 시간 및 걸음 수 계산
        Long walkTime = Duration.between(startTime, endTime).toSeconds();
        Integer walkSteps = 0; // 걸음 수 계산 필요

        step.updateStep(
                step.getTotalWalkSteps() + walkSteps,
                step.getTotalWalkTime() + walkTime,
                calculateGoalAchievementRate(step.getTotalWalkSteps() + walkSteps, user.getDailyGoalSteps())
        );

        step.endWalking();

        // 캐싱에 있는 시작 시간 제거 구현 필요
    }

    // 목표 달성률 계산 메서드
    private Float calculateGoalAchievementRate(Integer totalWalkSteps, Integer goal) {
        return (float) totalWalkSteps / goal * 100;
    }
}
