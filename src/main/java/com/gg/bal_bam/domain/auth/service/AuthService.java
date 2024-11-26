package com.gg.bal_bam.domain.auth.service;

import com.gg.bal_bam.domain.auth.dto.LoginRequest;
import com.gg.bal_bam.domain.auth.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}