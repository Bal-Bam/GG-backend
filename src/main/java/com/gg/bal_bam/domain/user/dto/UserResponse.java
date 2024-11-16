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
    private String profileImageUrl;

    private UserResponse(UUID userId, String username, String profileImageUrl) {
        this.userId = userId;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserResponse of(UUID userId, String username, String profileImageUrl) {
        return new UserResponse(userId, username, profileImageUrl);
    }
}
