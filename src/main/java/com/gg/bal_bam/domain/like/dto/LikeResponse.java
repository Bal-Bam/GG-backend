package com.gg.bal_bam.domain.like.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class LikeResponse {

    private Long postId;
    private boolean isLiked;
    private long likeCount;

    private LikeResponse(Long postId, boolean isLiked, long likeCount) {
        this.postId = postId;
        this.isLiked = isLiked;
        this.likeCount = likeCount;
    }

    public static LikeResponse of(Long postId, boolean isLiked, long likeCount) {
        return new LikeResponse(postId, isLiked, likeCount);
    }
}
