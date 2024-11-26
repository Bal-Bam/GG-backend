package com.gg.bal_bam.domain.user.service;

import com.gg.bal_bam.domain.user.dto.LoginRequest;
import com.gg.bal_bam.domain.user.dto.LoginResponse;
import com.gg.bal_bam.domain.user.model.User;

public interface UserService {
    void registerUser(String email, String username, String password);
    LoginResponse login(LoginRequest request);

    void validateEmail(String email);
    void validateUsername(String username);
}