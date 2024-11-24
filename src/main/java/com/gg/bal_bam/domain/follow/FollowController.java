package com.gg.bal_bam.domain.follow;

import com.gg.bal_bam.common.ResponseTemplate;
import com.gg.bal_bam.domain.follow.dto.FollowListResponse;
import com.gg.bal_bam.domain.follow.dto.FollowResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Follow API", description = "팔로우 요청과 팔로우 목록 추천 API를 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "팔로우 요청", description = "팔로우 요청을 합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "팔로우 요청 성공",
            useReturnTypeSchema = true
    )
    @PostMapping("/{targetUserId}")
    public ResponseTemplate<FollowResponse> follow(
            @Parameter(description = "팔로우 대상 유저 ID", example = "4ee3b4d7-2ac6-48ab-bda7-1a204b86d631")
            @PathVariable UUID targetUserId
            // userId 추가
    ) {
        UUID userId = UUID.fromString("a8a73cde-cbc1-48dc-91cb-93c1ba8fe8c2");

        return ResponseTemplate.ok(followService.follow(userId, targetUserId));
    }

    @Operation(summary = "언팔로우", description = "팔로우를 취소합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "언팔로우 성공",
            useReturnTypeSchema = true
    )
    @DeleteMapping("/{targetUserId}")
    public ResponseTemplate<FollowResponse> unfollow(
            @Parameter(description = "언팔로우 대상 유저 ID", example = "4ee3b4d7-2ac6-48ab-bda7-1a204b86d631")
            @PathVariable UUID targetUserId
            // userId 추가
    ) {
        UUID userId = UUID.fromString("a8a73cde-cbc1-48dc-91cb-93c1ba8fe8c2");

        return ResponseTemplate.ok(followService.unfollow(targetUserId, userId));
    }

    @Operation(summary = "팔로우 추천 목록 조회", description = "팔로우 추천 목록을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "팔로우 추천 목록 조회 성공",
            useReturnTypeSchema = true
    )
    @GetMapping("/recommendations")
    public ResponseTemplate<Page<FollowListResponse>> getRecommendations(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit
            //userId 추가
    ) {

        UUID userId = UUID.fromString("a8a73cde-cbc1-48dc-91cb-93c1ba8fe8c2");

        Pageable pageable = PageRequest.of(offset, limit);
        return ResponseTemplate.ok(followService.getFollowRecommendList(userId, pageable));
    }
}
