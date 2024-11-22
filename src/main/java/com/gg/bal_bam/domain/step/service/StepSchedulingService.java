package com.gg.bal_bam.domain.step.service;

import com.gg.bal_bam.domain.step.StepRepository;
import com.gg.bal_bam.domain.step.model.Step;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StepSchedulingService {

    private final StepRepository stepRepository;
    private final UserRepository userRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void createStep() {
        LocalDate today = LocalDate.now();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            stepRepository.findByUserAndDate(user, today)
                    .orElseGet(() -> stepRepository.save(Step.createStep(user, today)));
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 1 1 * *")
    public void deleteOldSteps() {
        LocalDate cutoffDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        stepRepository.deleteStepsOlderThan(cutoffDate);
    }
}
