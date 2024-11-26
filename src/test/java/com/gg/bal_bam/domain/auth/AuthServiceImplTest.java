package com.gg.bal_bam.domain.auth;

import com.gg.bal_bam.domain.auth.dto.LoginRequest;
import com.gg.bal_bam.domain.auth.dto.LoginResponse;
import com.gg.bal_bam.domain.auth.service.AuthService;
import com.gg.bal_bam.domain.auth.token.AuthTokenProvider;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest

@Transactional // 모든 테스트 메서드 실행 후 롤백
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthTokenProvider authTokenProvider;

    @Test
    void login_success() {
        // Given: 사용자를 저장
        User mockUser = User.createUser("test@example.com", passwordEncoder.encode("password123"), "testUser");
        userRepository.save(mockUser);

        // When: 로그인 요청
        LoginRequest loginRequest = new LoginRequest("testUser", "password123");
        LoginResponse response = authService.login(loginRequest);

        // Then: 로그인 결과 확인
        assertEquals(mockUser.getUsername(), response.getUser().getUsername());
        assertEquals(mockUser.getEmail(), response.getUser().getEmail());
        assertEquals(response.getAccessToken().length() > 10, true); // 토큰이 생성되었는지 확인
    }

    @Test
    void login_invalidPassword() {
        // Given: 사용자를 저장
        User mockUser = User.createUser("test@example.com", passwordEncoder.encode("password123"), "testUser");
        userRepository.save(mockUser);

        // When & Then: 잘못된 비밀번호로 로그인 시도 시 예외 발생
        LoginRequest loginRequest = new LoginRequest("testUser", "wrongPassword");
        CustomException exception = assertThrows(CustomException.class, () -> authService.login(loginRequest));
        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    void login_userNotFound() {
        // Given: 존재하지 않는 사용자

        // When & Then: 사용자 조회 실패로 예외 발생
        LoginRequest loginRequest = new LoginRequest("nonExistentUser", "password123");
        CustomException exception = assertThrows(CustomException.class, () -> authService.login(loginRequest));
        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public AuthTokenProvider authTokenProvider() {
            return new AuthTokenProvider("926D96C90030DD58429D2751AC1BDBBC", 18000000L);
        }
    }

}
