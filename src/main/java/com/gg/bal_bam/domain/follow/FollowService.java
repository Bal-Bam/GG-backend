package com.gg.bal_bam.domain.follow;

import com.gg.bal_bam.domain.follow.dto.FollowResponse;
import com.gg.bal_bam.domain.follow.model.Follow;
import com.gg.bal_bam.domain.follow.model.PendingFollow;
import com.gg.bal_bam.domain.follow.repository.FollowRepository;
import com.gg.bal_bam.domain.follow.repository.PendingFollowRepository;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PendingFollowRepository pendingFollowRepository;

    @Transactional
    public FollowResponse follow(UUID followerId, UUID followingId) {

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + followerId));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + followingId));

        // 이미 팔로우 중인지 확인
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new CustomException("이미 팔로우 중입니다.");
        }

        // 팔로우 대상이 비공개 계정인 경우
        if (following.getIsPrivate()) {
            // 팔로우 요청 중인지 확인
            if (!pendingFollowRepository.existsByRequesterAndTarget(follower, following)) {
                PendingFollow followRequest = PendingFollow.createFollowRequest(follower, following);
                pendingFollowRepository.save(followRequest);
            }
            return FollowResponse.of(followingId, false, true);
        }

        // 공개 계정인 경우 팔로우
        Follow follow = Follow.createFollow(follower, following);
        followRepository.save(follow);

        return FollowResponse.of(followingId, true, false);
    }
}