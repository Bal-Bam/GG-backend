package com.gg.bal_bam.domain.follow.model;

import com.gg.bal_bam.common.entity.BaseTimeEntity;
import com.gg.bal_bam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower; // 팔로우 하는 사람 (나)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User following; // 팔로우 당하는 사람 (상대방)

    private Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    public static Follow createFollow(User follower, User following) {
        return new Follow(follower, following);
    }
}
