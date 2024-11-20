package com.gg.bal_bam.domain.follow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PendingFollowResponse {

    private UUID requesterId; // 팔로우 요청자 userId

    private PendingFollowResponse(UUID requesterId) {
        this.requesterId = requesterId;
    }

    public static PendingFollowResponse of(UUID requesterId) {
        return new PendingFollowResponse(requesterId);
    }
}
