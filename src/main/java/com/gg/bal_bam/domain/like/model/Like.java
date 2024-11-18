package com.gg.bal_bam.domain.like.model;

import com.gg.bal_bam.domain.post.model.Post;
import com.gg.bal_bam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private boolean isLiked;

    private Like(User user, Post post) {
        this.user = user;
        this.post = post;
        this.isLiked = true;
    }

    public static Like createLike(User user, Post post) {
        return new Like(user, post);
    }
}
