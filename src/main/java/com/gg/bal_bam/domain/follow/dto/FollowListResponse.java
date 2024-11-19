package com.gg.bal_bam.domain.follow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class FollowListResponse {

    private UUID userId;
    private String username;
    private String profileImage;
    private boolean isFollowing; // 현재 유저가 팔로우 하고 있는지 여

    private FollowListResponse(UUID userId, String username, String profileImage, boolean isFollowing) {
        this.userId = userId;
        this.username = username;
        this.profileImage = profileImage;
        this.isFollowing = isFollowing;
    }

    public static FollowListResponse of(UUID userId, String username, String profileImage, boolean isFollowing) {
        return new FollowListResponse(userId, username, profileImage, isFollowing);
    }
}
