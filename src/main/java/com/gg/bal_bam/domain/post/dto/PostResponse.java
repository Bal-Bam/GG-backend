package com.gg.bal_bam.domain.post.dto;

import com.gg.bal_bam.domain.user.dto.TaggedUserRequest;
import com.gg.bal_bam.domain.user.dto.UserResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PostResponse {

    private Long postId;
    private UserResponse user; // 게시글 작성자
    private String content;
    private Double latitude;
    private Double longitude;
    private List<> taggedUsers; // 태그된 사용자 목록
    private LocalDateTime createdAt;
    private List<PostResponse> childPosts; // 자식 게시글 목록

    private PostResponse(Long postId, UserResponse user, String content, Double latitude, Double longitude, List<TaggedUserRequest> taggedUsers, LocalDateTime createdAt, List<PostResponse> childPosts) {
        this.postId = postId;
        this.user = user;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taggedUsers = taggedUsers;
        this.createdAt = createdAt;
        this.childPosts = childPosts;
    }

    public static PostResponse of(Long postId, UserResponse user, String content, Double latitude, Double longitude, List<TaggedUserRequest> taggedUsers, LocalDateTime createdAt, List<PostResponse> childPosts) {
        return new PostResponse(postId, user, content, latitude, longitude, taggedUsers, createdAt, childPosts);
    }
}
