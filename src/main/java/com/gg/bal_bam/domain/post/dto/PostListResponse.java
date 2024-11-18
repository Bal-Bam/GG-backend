package com.gg.bal_bam.domain.post.dto;

import com.gg.bal_bam.domain.post.model.Post;
import com.gg.bal_bam.domain.post.model.PostTag;
import com.gg.bal_bam.domain.user.dto.TaggedUserResponse;
import com.gg.bal_bam.domain.user.dto.UserPostResponse;
import com.gg.bal_bam.domain.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PostListResponse {

    private Long postId;
    private UserPostResponse user; // 게시글 작성자
    private String content;
    private Double latitude;
    private Double longitude;
    private List<TaggedUserResponse> taggedUsers; // 태그된 사용자 목록
    private LocalDateTime createdAt;

    private PostListResponse(Long postId, UserPostResponse user, String content, Double latitude, Double longitude, List<TaggedUserResponse> taggedUsers, LocalDateTime createdAt) {
        this.postId = postId;
        this.user = user;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taggedUsers = taggedUsers;
        this.createdAt = createdAt;
    }

    public static PostListResponse of(Post post, List<PostTag> postTags) {

        User user = post.getUser();

        UserPostResponse userPostResponse = UserPostResponse.of(
                user.getId(),
                user.getUsername(),
                user.getProfileImage()
        );

        List<TaggedUserResponse> taggedUserResponses = postTags.stream()
                .map(postTag -> TaggedUserResponse.of(
                        postTag.getTaggedUser().getId(),
                        postTag.getTaggedUser().getUsername()
                ))
                .toList();

        return new PostListResponse(
                post.getId(),
                userPostResponse,
                post.getContent(),
                post.getLatitude(),
                post.getLongitude(),
                taggedUserResponses,
                post.getCreatedAt()
        );
    }
}
