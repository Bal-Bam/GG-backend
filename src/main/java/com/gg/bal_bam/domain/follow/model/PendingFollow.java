package com.gg.bal_bam.domain.follow.model;

import com.gg.bal_bam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PendingFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester; // 팔로우 요청을 보낸 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private User target; // 팔로우 요청을 받은 사람

    private PendingFollow(User requester, User target) {
        this.requester = requester;
        this.target = target;
    }

    public static PendingFollow createFollowRequest(User requester, User target) {
        return new PendingFollow(requester, target);
    }
}
