package com.gg.bal_bam.domain.auth.controller;

import com.gg.bal_bam.common.ResponseTemplate;
import com.gg.bal_bam.domain.auth.dto.LoginRequest;
import com.gg.bal_bam.domain.auth.dto.LoginResponse;
import com.gg.bal_bam.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 특정 도메인만 허용
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseTemplate<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseTemplate.ok(loginResponse);
    }
}