package com.gg.bal_bam.domain.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {
    private UUID userId;
    private String username;
    private String profileImage;

    private UserResponse(UUID userId, String username, String profileImage) {
        this.userId = userId;
        this.username = username;
        this.profileImage = profileImage;
    }

    public static UserResponse of(UUID userId, String username, String profileImage) {
        return new UserResponse(userId, username, profileImage);
    }
}
