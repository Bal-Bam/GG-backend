package com.gg.bal_bam.domain.post.model;

import com.gg.bal_bam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id", nullable = false)
        private Post post;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "tagged_user_id", nullable = false)
        private User taggedUser;

        private PostTag(Post post, User taggedUser) {
            this.post = post;
            this.taggedUser = taggedUser;
        }

        public static PostTag createPostTag(Post post, User taggedUser) {
            return new PostTag(post, taggedUser);
        }
}
