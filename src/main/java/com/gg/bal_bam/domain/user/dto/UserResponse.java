package com.gg.bal_bam.domain.user.dto;

import com.gg.bal_bam.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {
    private UUID userId;            // 사용자 고유 ID
    private String email;       // 이메일 (사용자 확인용)
    private String username;    // 사용자 이름
    private String profileImage; // 프로필 이미지
    private String description;  // 자기 소개
    private Integer dailyGoalSteps; // 목표 걸음 수
    private Boolean isPrivate;  // 계정 공개 여부

    public UserResponse(UUID userId, String email, String username, String profileImage, String description, Integer dailyGoalSteps, Boolean isPrivate) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.profileImage = profileImage;
        this.description = description;
        this.dailyGoalSteps = dailyGoalSteps;
        this.isPrivate = isPrivate;
    }

    // User 엔티티를 받아 UserResponse를 생성하는 생성자
    private UserResponse(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.profileImage = user.getProfileImage();
        this.description = user.getDescription();
        this.dailyGoalSteps = user.getDailyGoalSteps();
        this.isPrivate = user.getIsPrivate();
    }

    // 정적 팩토리 메서드: 개별 필드로 생성
    public static UserResponse of(UUID userId, String email, String username, String profileImage, String description, Integer dailyGoalSteps, Boolean isPrivate) {
        return new UserResponse(userId, email, username, profileImage, description, dailyGoalSteps, isPrivate);
    }

    // 정적 팩토리 메서드: User 엔티티로 생성
    public static UserResponse of(User user) {
        return new UserResponse(user);
    }

}