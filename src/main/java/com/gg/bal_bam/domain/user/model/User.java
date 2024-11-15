package com.gg.bal_bam.domain.user.model;

import com.gg.bal_bam.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true) // 일반 로그인에서만 사용
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    private String profileImage;

    @Column(length = 500)
    private String description;

    private Integer dailyGoalSteps;

    @Column(nullable = false)
    private Boolean isPrivate = false; // default: 공개 계정

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Column(nullable = true) // 소셜 로그인에서만 사용
    private String providerId;

    // 필수 필드
    private User(String email, String password, String username, AuthProvider provider) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.provider = provider;
    }

    // 일반 로그인
    public static User createUser(String email, String password, String username) {
        return new User(email, password, username, AuthProvider.LOCAL);
    }

    // 소셜 로그인
    public static User createSocialUser(String email, String username, AuthProvider provider, String providerId) {
        User user = new User(email, null, username, provider); // password는 null
        user.providerId = providerId;
        return user;
    }

    // 회원정보 수정
    public void updateProfile(String username, String profileImage, String description, Integer dailyGoalSteps, Boolean isPrivate) {
        if (username != null) this.username = username;
        if (profileImage != null) this.profileImage = profileImage;
        if (description != null) this.description = description;
        if (dailyGoalSteps != null) this.dailyGoalSteps = dailyGoalSteps;
        if (isPrivate != null) this.isPrivate = isPrivate;
    }

    // 비밀번호 변경
    public void changePassword(String currentPassword, String newPassword) {
        if (!this.password.equals(currentPassword)) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        this.password = newPassword;
    }
}