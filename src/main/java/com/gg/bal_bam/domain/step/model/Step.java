package com.gg.bal_bam.domain.step.model;


import com.gg.bal_bam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime date; // 날짜

    private Integer totalWalkSteps; // 총 걸음 수
    private Integer totalWalkTime; // 총 걸음 시간

    private boolean isWalked = false; // 산책 여부

    private Float goalAchievementRate; // 목표 달성률

    private Step(User user, LocalDateTime date, Integer totalWalkSteps, Integer totalWalkTime, boolean isWalked, Float goalAchievementRate) {
        this.user = user;
        this.date = date;
        this.totalWalkSteps = totalWalkSteps;
        this.totalWalkTime = totalWalkTime;
        this.isWalked = isWalked;
        this.goalAchievementRate = goalAchievementRate;
    }

    public static class Builder {
        private User user;
        private LocalDateTime date;
        private Integer totalWalkSteps;
        private Integer totalWalkTime;
        private boolean isWalked = false;
        private Float goalAchievementRate;

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder totalWalkSteps(Integer totalWalkSteps) {
            this.totalWalkSteps = totalWalkSteps;
            return this;
        }

        public Builder totalWalkTime(Integer totalWalkTime) {
            this.totalWalkTime = totalWalkTime;
            return this;
        }

        public Builder isWalked(boolean isWalked) {
            this.isWalked = isWalked;
            return this;
        }

        public Builder goalAchievementRate(Float goalAchievementRate) {
            this.goalAchievementRate = goalAchievementRate;
            return this;
        }

        public Step build() {
            return new Step(user, date, totalWalkSteps, totalWalkTime, isWalked, goalAchievementRate);
        }
    }


    public void updateStep(Integer totalWalkSteps, Integer totalWalkTime, boolean isWalked, Float goalAchievementRate) {
        if (totalWalkSteps != null) {
            this.totalWalkSteps = totalWalkSteps;
        }

        if (totalWalkTime != null) {
            this.totalWalkTime = totalWalkTime;
        }

        this.isWalked = isWalked;

        if (goalAchievementRate != null) {
            this.goalAchievementRate = goalAchievementRate;
        }
    }
}
