package com.gg.bal_bam.domain.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPostResponse {
    private UUID userId;
    private String username;
    private String profileImage;

    private UserPostResponse(UUID userId, String username, String profileImage) {
        this.userId = userId;
        this.username = username;
        this.profileImage = profileImage;
    }

    public static UserPostResponse of(UUID userId, String username, String profileImage) {
        return new UserPostResponse(userId, username, profileImage);
    }
}
