package com.gg.bal_bam.domain.auth.service;

import com.gg.bal_bam.domain.auth.dto.LoginRequest;
import com.gg.bal_bam.domain.auth.dto.LoginResponse;
import com.gg.bal_bam.domain.auth.token.AuthTokenProvider;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.dto.UserResponse;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthTokenProvider authTokenProvider;

    @Override
    public LoginResponse login(LoginRequest request) {
        // 사용자 조회
        User user = isEmail(request.getIdentifier())
                ? userRepository.findByEmail(request.getIdentifier()).orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."))
                : userRepository.findByUsername(request.getIdentifier()).orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성 (userId 사용)
        String accessToken = authTokenProvider.createAuthToken(user.getId().toString()).getToken();
//        String accessToken = "dummy-token-for-testing";

        // UserResponse로 변환 후 LoginResponse 생성
        UserResponse userResponse = UserResponse.of(user);
        return LoginResponse.of(userResponse, accessToken);
    }

    private boolean isEmail(String input) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return input.matches(emailRegex);
    }
}