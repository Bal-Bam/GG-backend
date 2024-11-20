package com.gg.bal_bam.domain.like;

import com.gg.bal_bam.common.ResponseTemplate;
import com.gg.bal_bam.domain.like.dto.LikeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Like API", description = "좋아요 관련 API를 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/likes")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 등록", description = "게시물에 좋아요를 등록합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "좋아요 등록 성공",
            useReturnTypeSchema = true
    )
    @PostMapping("/{postId}")
    public ResponseTemplate<LikeResponse> likePost(
            @PathVariable Long postId
    ) {
        UUID userId = null;
        return ResponseTemplate.ok(likeService.like(postId, userId));
    }
}
