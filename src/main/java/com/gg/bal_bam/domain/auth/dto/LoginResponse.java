package com.gg.bal_bam.domain.auth.dto;

import com.gg.bal_bam.domain.user.dto.UserResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {
    private UserResponse user;
    private String accessToken;

    private LoginResponse(UserResponse user, String accessToken) {
        this.user = user;
        this.accessToken = accessToken;
    }

    public static LoginResponse of(UserResponse user, String accessToken) {
        return new LoginResponse(user, accessToken);
    }
}