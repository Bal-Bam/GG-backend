package com.gg.bal_bam.domain.follow;

import com.gg.bal_bam.domain.follow.dto.FollowListResponse;
import com.gg.bal_bam.domain.follow.dto.PendingFollowResponse;
import com.gg.bal_bam.domain.follow.dto.FollowResponse;
import com.gg.bal_bam.domain.follow.model.Follow;
import com.gg.bal_bam.domain.follow.model.PendingFollow;
import com.gg.bal_bam.domain.follow.repository.FollowRepository;
import com.gg.bal_bam.domain.follow.repository.PendingFollowRepository;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    public FollowResponse unfollow(UUID followerId, UUID followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + followerId));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + followingId));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new CustomException("팔로우 중이 아닙니다."));

        followRepository.delete(follow);

        return FollowResponse.of(followingId, false, false);
    }

    public Page<FollowListResponse> getFollowRecommendList(UUID userId, Pageable pageable) {

        List<UUID> followingUserIds = followRepository.findFollowedIdByFollowerId(userId);

        Page<User> recommendUsers = userRepository.findRandomUsers(userId, followingUserIds, pageable);

        return recommendUsers.map(user -> FollowListResponse.of(
                user.getId(),
                user.getUsername(),
                user.getProfileImage(),
                followingUserIds.contains(user.getId())
        ));
    }

    public PendingFollowResponse acceptFollowRequest(UUID userId, UUID requesterId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + userId));

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new CustomException("수락하는 사용자가 존재하지 않습니다. RequesterId: " + requesterId));

        PendingFollow pendingFollow = pendingFollowRepository.findByRequesterAndTarget(requester, user)
                .orElseThrow(() -> new CustomException("해당 요청이 존재하지 않습니다."));

        Follow follow = Follow.createFollow(requester, user);
        followRepository.save(follow);

        pendingFollowRepository.delete(pendingFollow);

        return PendingFollowResponse.of(requesterId);
    }

    public void rejectFollowRequest(UUID userId, UUID requesterId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + userId));

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new CustomException("거절하는 사용자가 존재하지 않습니다. RequesterId: " + requesterId));

        PendingFollow pendingFollow = pendingFollowRepository.findByRequesterAndTarget(requester, user)
                .orElseThrow(() -> new CustomException("해당 요청이 존재하지 않습니다."));

        pendingFollowRepository.delete(pendingFollow);

    }
}
