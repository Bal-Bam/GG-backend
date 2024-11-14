package com.gg.bal_bam.domain.post.model;

import com.gg.bal_bam.common.entity.BaseTimeEntity;
import com.gg.bal_bam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Post parentPost; // 부모 게시글

    private String content; // 내용
    private Boolean isOriginal; // 메인 게시글인지 여부

    private Double latitude; // 위도
    private Double longitude; // 경도

    private Post(User user, Post parentPost, String content, Boolean isOriginal, Double latitude, Double longitude) {
        this.user = user;
        this.parentPost = parentPost;
        this.content = content;
        this.isOriginal = isOriginal;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Post createPost(User user, Post parentPost, String content, Boolean isOriginal, Double latitude, Double longitude) {
        return new Post(user, parentPost, content, isOriginal, latitude, longitude);
    }

}
