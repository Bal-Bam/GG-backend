package com.gg.bal_bam.domain.auth;

import com.gg.bal_bam.domain.auth.token.AuthToken;
import com.gg.bal_bam.domain.auth.token.AuthTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenProviderTest {

    private AuthTokenProvider authTokenProvider;
    private final String tokenSecret = "0123456789abcdef0123456789abcdef"; // 최소 32바이트
    private final long tokenExpiry = 60000L; // 1분

    @BeforeEach
    void setUp() {
        authTokenProvider = new AuthTokenProvider(tokenSecret, tokenExpiry);
    }

    @Test
    void testCreateAuthToken() {
        // Given
        String userId = "testUser";
        String role = "member";

        // When
        AuthToken authToken = authTokenProvider.createAuthToken(userId, role);

        // Then
        assertNotNull(authToken, "토큰 생성 실패");
        assertTrue(authToken.validate(), "토큰이 유효하지 않습니다.");
        assertEquals(userId, authToken.getTokenClaims().getSubject(), "User ID가 다릅니다.");
        assertEquals(role, authToken.getTokenClaims().get("role"), "Role이 다릅니다.");
    }

    @Test
    void testConvertAuthToken() {
        // Given
        String userId = "testUser";
        AuthToken authToken = authTokenProvider.createAuthToken(userId, "member");
        String tokenString = authToken.getToken();

        // When
        AuthToken convertedToken = authTokenProvider.convertAuthToken(tokenString);

        // Then
        assertNotNull(convertedToken, "토큰 변환 실패");
        assertTrue(convertedToken.validate(), "변환된 토큰이 유효하지 않습니다.");
    }

    @Test
    void testGetAuthentication() {
        // Given
        String userId = "testUser";
        String role = "member";
        AuthToken authToken = authTokenProvider.createAuthToken(userId, role);

        // When
        Authentication authentication = authTokenProvider.getAuthentication(authToken);

        // Then
        assertNotNull(authentication, "인증 정보 생성 실패");
        assertEquals(userId, authentication.getName(), "인증된 사용자 ID가 다릅니다.");
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("member")), "Role이 인증 정보에 포함되지 않았습니다.");
    }
}