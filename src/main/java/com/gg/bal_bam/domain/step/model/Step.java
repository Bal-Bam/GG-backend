package com.gg.bal_bam.domain.step.model;


import com.gg.bal_bam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자

    @Column(nullable = false)
    private LocalDate date; // 날짜

    private Integer totalWalkSteps = 0; // 총 걸음 수
    private Long totalWalkTime = 0L; // 총 걸음 시간

    private Float goalAchievementRate = 0.0f; // 목표 달성률

    private Step(User user, LocalDate date) {
        this.user = user;
        this.date = date;
    }

    public static Step createStep(User user, LocalDate date) {
        return new Step(user, date);
    }


    public void updateStep(Integer totalWalkSteps, Long totalWalkTime, Float goalAchievementRate) {
        if (totalWalkSteps != null) {
            this.totalWalkSteps = totalWalkSteps;
        }

        if (totalWalkTime != null) {
            this.totalWalkTime = totalWalkTime;
        }

        if (goalAchievementRate != null) {
            this.goalAchievementRate = goalAchievementRate;
        }
    }
}
