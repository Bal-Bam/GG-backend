package com.gg.bal_bam.domain.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaggedUserResponse {

    private UUID userId;
    private String username;

    private TaggedUserResponse(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public static TaggedUserResponse of(UUID userId, String username) {
        return new TaggedUserResponse(userId, username);
    }
}
