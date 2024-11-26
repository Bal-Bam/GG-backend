package com.gg.bal_bam.domain.user.model;

import com.gg.bal_bam.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;


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

    // 회원정보 수정
    public void updateProfile(String username, String profileImage, String description, Integer dailyGoalSteps, Boolean isPrivate) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("유저네임을 입력해주세요");
        }
        this.profileImage = profileImage;
        this.description = description;
        this.dailyGoalSteps = dailyGoalSteps;
        this.isPrivate = isPrivate;
    }

//    // 비밀번호 변경
//    public void changePassword(String currentPassword, String newPassword) {
//        // 비밀번호가 null인지 확인
//        if (currentPassword == null || newPassword == null) {
//            throw new IllegalArgumentException("값이 null");
//        }
//
//        // 현재 비밀번호가 일치하는지 확인
//        if (!this.password.equals(currentPassword)) {
//            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다");
//        }
//
//        // 새 비밀번호 설정
//        this.password = newPassword;
//    }
}