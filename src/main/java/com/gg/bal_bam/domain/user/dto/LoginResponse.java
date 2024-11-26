package com.gg.bal_bam.domain.user.dto;

import com.gg.bal_bam.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {
    private User user;

    private LoginResponse(User user) {
        this.user = user;
    }

    public static LoginResponse of(User user) {
        return new LoginResponse(user);
    }
}