package com.gg.bal_bam.domain.follow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class FollowResponse {

    private UUID targetId; // 팔로우 대상 userId
    private boolean isFollowed; // 팔로우 여부 -> 성공하면 true
    private boolean isPending; // 팔로우 요청 대기 여부 -> 대기 중이면 true

    private FollowResponse(UUID targetId, boolean isFollowed, boolean isPending) {
        this.targetId = targetId;
        this.isFollowed = isFollowed;
        this.isPending = isPending;
    }

    public static FollowResponse of(UUID targetId, boolean isFollowed, boolean isPending) {
        return new FollowResponse(targetId, isFollowed, isPending);
    }
}
