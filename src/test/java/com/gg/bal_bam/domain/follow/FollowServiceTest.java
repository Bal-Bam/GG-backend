package com.gg.bal_bam.domain.follow;

import com.gg.bal_bam.domain.follow.dto.FollowListResponse;
import com.gg.bal_bam.domain.follow.dto.FollowResponse;
import com.gg.bal_bam.domain.follow.dto.PendingFollowResponse;
import com.gg.bal_bam.domain.follow.model.Follow;
import com.gg.bal_bam.domain.follow.model.PendingFollow;
import com.gg.bal_bam.domain.follow.repository.FollowRepository;
import com.gg.bal_bam.domain.follow.repository.PendingFollowRepository;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @InjectMocks
    private FollowService followService;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private PendingFollowRepository pendingFollowRepository;

    @Mock
    private UserRepository userRepository;

    private User follower;
    private UUID followerId;

    @Mock
    private User publicFollowing;
    private UUID publicFollowingId;

    @Mock
    private User privateFollowing;
    private UUID privateFollowingId;

    @Mock
    private User user;
    private UUID userId;

    @Mock
    private User requester;
    private UUID requesterId;

    @BeforeEach
    public void setUp() {
        followerId = UUID.randomUUID();
        publicFollowingId = UUID.randomUUID();
        privateFollowingId = UUID.randomUUID();

        // 공개 계정
        publicFollowing = mock(User.class);
        ReflectionTestUtils.setField(publicFollowing, "id", publicFollowingId);
        ReflectionTestUtils.setField(publicFollowing, "isPrivate", false);
        lenient().when(publicFollowing.getIsPrivate()).thenReturn(false);

        // 비공개 계정
        privateFollowing = mock(User.class);
        ReflectionTestUtils.setField(privateFollowing, "id", privateFollowingId);
        ReflectionTestUtils.setField(privateFollowing, "isPrivate", true);
        lenient().when(privateFollowing.getIsPrivate()).thenReturn(true);

        // 팔로워
        follower = User.createUser("follower@example.com", "password","follower");
        ReflectionTestUtils.setField(follower, "id", followerId);

        userId = UUID.randomUUID();
        requesterId = UUID.randomUUID();

        user = User.createUser("user@example.com", "password", "user");
        requester = User.createUser("requester@example.com", "password", "requester");
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(requester, "id", requesterId);
    }

    @Test
    @DisplayName("공개 계정 팔로우 성공")
    void followPublicSuccess() {
        //given
        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(publicFollowingId)).thenReturn(Optional.of(publicFollowing));
        when(followRepository.existsByFollowerAndFollowing(follower, publicFollowing)).thenReturn(false);

        //when
        FollowResponse response = followService.follow(followerId, publicFollowingId);

        //then
        verify(followRepository, times(1)).save(any(Follow.class));
        assertThat(response.isFollowed()).isTrue();
        assertThat(response.isPending()).isFalse();
    }

    @Test
    @DisplayName("비공개 계정 팔로우 성공")
    void followPrivateSuccess() {
        //given
        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(privateFollowingId)).thenReturn(Optional.of(privateFollowing));
        when(followRepository.existsByFollowerAndFollowing(follower, privateFollowing)).thenReturn(false);
        when(pendingFollowRepository.existsByRequesterAndTarget(follower, privateFollowing)).thenReturn(false);

        //when
        FollowResponse response = followService.follow(followerId, privateFollowingId);

        //then
        verify(pendingFollowRepository, times(1)).save(any(PendingFollow.class));
        assertThat(response.isFollowed()).isFalse();
        assertThat(response.isPending()).isTrue();
    }

    @Test
    @DisplayName("이미 팔로우 중인데 팔로우 요청을 할 경우")
    void followAlreadyFollowing() {
        //given
        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(publicFollowingId)).thenReturn(Optional.of(publicFollowing));
        when(followRepository.existsByFollowerAndFollowing(follower, publicFollowing)).thenReturn(true);

        //when / then
        assertThatThrownBy(() -> followService.follow(followerId, publicFollowingId))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 팔로우 중입니다.");
        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    @DisplayName("비공개 계정에 팔로우 요청 중인데 팔로우 요청을 할 경우")
    void pendingRequestAlreadyExists() {
        //given
        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(privateFollowingId)).thenReturn(Optional.of(privateFollowing));
        when(followRepository.existsByFollowerAndFollowing(follower, privateFollowing)).thenReturn(false);
        when(pendingFollowRepository.existsByRequesterAndTarget(follower, privateFollowing)).thenReturn(true);

        //when
        FollowResponse response = followService.follow(followerId, privateFollowingId);

        // then
        verify(pendingFollowRepository, never()).save(any(PendingFollow.class));
        assertThat(response.isFollowed()).isFalse();
        assertThat(response.isPending()).isTrue();
    }

    @Test
    @DisplayName("팔로우 대상이 존재하지 않을 경우")
    void followNonExistUser() {
        //given
        when(userRepository.findById(followerId)).thenReturn(Optional.empty());

        //when / then
        assertThatThrownBy(() -> followService.follow(followerId, publicFollowingId))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 유저가 존재하지 않습니다. UserId: " + followerId);
    }

    @Test
    @DisplayName("팔로우 취소 성공")
    void unfollowSuccess() {
        //given
        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(publicFollowingId)).thenReturn(Optional.of(publicFollowing));
        Follow follow = Follow.createFollow(follower, publicFollowing);
        when(followRepository.findByFollowerAndFollowing(follower, publicFollowing)).thenReturn(Optional.of(follow));

        //when
        FollowResponse response = followService.unfollow(followerId, publicFollowingId);

        //then
        verify(followRepository, times(1)).delete(follow);
        assertThat(response.isFollowed()).isFalse();
        assertThat(response.isPending()).isFalse();
    }

    @Test
    @DisplayName("팔로우 중이 아닌데 팔로우 취소를 할 경우")
    void unfollowNonExistFollowing() {
        //given
        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(publicFollowingId)).thenReturn(Optional.of(publicFollowing));
        when(followRepository.findByFollowerAndFollowing(follower, publicFollowing)).thenReturn(Optional.empty());

        //when / then
        assertThatThrownBy(() -> followService.unfollow(followerId, publicFollowingId))
                .isInstanceOf(CustomException.class)
                .hasMessage("팔로우 중이 아닙니다.");

    }

    @Test
    @DisplayName("팔로우 추천 리스트 조회 성공")
    void getFollowRecommendListSuccess() {
        //given
        UUID userId = UUID.randomUUID();
        int offset = 0;
        int limit = 5;
        Pageable pageable = PageRequest.of(offset, limit);

        User user1 = User.createUser("user1@example.com", "password1", "user1");
        User user2 = User.createUser("user2@example.com", "password2", "user2");
        ReflectionTestUtils.setField(user1, "id", UUID.randomUUID());
        ReflectionTestUtils.setField(user2, "id", UUID.randomUUID());

        Page<User> recommendUsers = new PageImpl<>(List.of(user2), pageable, 1);
        List<UUID> followingUserIds = List.of(user1.getId());

        when(followRepository.findFollowedIdByFollowerId(userId)).thenReturn(followingUserIds);
        when(userRepository.findRandomUsers(eq(userId), eq(followingUserIds), any(Pageable.class))).thenReturn(recommendUsers);

        //when
        Page<FollowListResponse> responses = followService.getFollowRecommendList(userId, pageable);

        //then
        assertThat(responses).isNotNull();
        assertThat(responses.getContent()).hasSize(1);
        assertThat(responses.getContent().get(0).getUserId()).isEqualTo(user2.getId());
    }

    @Test
    @DisplayName("팔로우 요청 수락 성공")
    void acceptFollowRequestSuccess() {
        //given
        PendingFollow pendingFollow = PendingFollow.createFollowRequest(requester, user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(pendingFollowRepository.findByRequesterAndTarget(requester, user)).thenReturn(Optional.of(pendingFollow));

        //when
        PendingFollowResponse response = followService.acceptFollowRequest(userId, requesterId);

        //then
        verify(followRepository, times(1)).save(any(Follow.class));
        verify(pendingFollowRepository, times(1)).delete(pendingFollow);
        assertThat(response).isNotNull();
        assertThat(response.getRequesterId()).isEqualTo(requesterId);

    }

    @Test
    @DisplayName("팔로우 요청 수락 시 요청이 존재하지 않을 경우")
    void acceptFollowRequestNonExistRequest() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(pendingFollowRepository.findByRequesterAndTarget(requester, user)).thenReturn(Optional.empty());

        //when / then
        assertThatThrownBy(() -> followService.acceptFollowRequest(userId, requesterId))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 요청이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("팔로우 요청 수락 시 요청자가 존재하지 않을 경우")
    void acceptFollowRequestNonExistRequester() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        //when / then
        assertThatThrownBy(() -> followService.acceptFollowRequest(userId, requesterId))
                .isInstanceOf(CustomException.class)
                .hasMessage("수락하는 사용자가 존재하지 않습니다. RequesterId: " + requesterId);
    }

    @Test
    @DisplayName("팔로우 요청 수락 시 팔로우 대상이 존재하지 않을 경우")
    void acceptFollowRequestNonExistUser() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when / then
        assertThatThrownBy(() -> followService.acceptFollowRequest(userId, requesterId))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 유저가 존재하지 않습니다. UserId: " + userId);
    }

    @Test
    @DisplayName("팔로우 요청 거절 성공")
    void rejectFollowRequestSuccess() {
        //given
        PendingFollow pendingFollow = PendingFollow.createFollowRequest(requester, user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(pendingFollowRepository.findByRequesterAndTarget(requester, user)).thenReturn(Optional.of(pendingFollow));

        //when
        followService.rejectFollowRequest(userId, requesterId);

        //then
        verify(pendingFollowRepository, times(1)).delete(pendingFollow);
    }

    @Test
    @DisplayName("팔로우 요청 거절 시 요청이 존재하지 않을 경우")
    void rejectFollowRequestNonExistRequest() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(pendingFollowRepository.findByRequesterAndTarget(requester, user)).thenReturn(Optional.empty());

        //when / then
        assertThatThrownBy(() -> followService.rejectFollowRequest(userId, requesterId))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 요청이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("팔로우 요청 거절 시 요청자가 존재하지 않을 경우")
    void rejectFollowRequestNonExistRequester() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        //when / then
        assertThatThrownBy(() -> followService.rejectFollowRequest(userId, requesterId))
                .isInstanceOf(CustomException.class)
                .hasMessage("거절하는 사용자가 존재하지 않습니다. RequesterId: " + requesterId);
    }

    @Test
    @DisplayName("팔로우 요청 거절 시 팔로우 대상이 존재하지 않을 경우")
    void rejectFollowRequestNonExistUser() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when / then
        assertThatThrownBy(() -> followService.rejectFollowRequest(userId, requesterId))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 유저가 존재하지 않습니다. UserId: " + userId);
    }
}