package com.gg.bal_bam.domain.auth.token;

import com.gg.bal_bam.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;



@Slf4j
@Component
public class AuthTokenProvider {

    private final Key key; // HMAC SHA-256 Key
    private final long tokenExpiry; // 토큰 만료 시간
    private static final String AUTHORITIES_KEY = "role";

    public AuthTokenProvider(@Value("${app.auth.tokenSecret}") String tokenSecret, @Value("${app.auth.tokenExpiry}") long tokenExpiry) {
        this.key = Keys.hmacShaKeyFor(tokenSecret.getBytes());
        this.tokenExpiry = tokenExpiry;
    }

    /**
     * 토큰 생성
     */
    private AuthToken createAuthTokenInternal(String userId, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpiry);
        return new AuthToken(userId, role, expiryDate, key);
    }

    // 일반 회원
    public AuthToken createAuthToken(String userId) {
        return createAuthTokenInternal(userId, "member");
    }

    // 추후 관리자 등
    public AuthToken createAuthToken(String userId, String role) {
        return createAuthTokenInternal(userId, role);
    }

    /**
     * 기존 토큰 변환
     */
    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    /**
     * 토큰 검증 및 인증 정보 반환
     */
    public Authentication getAuthentication(AuthToken authToken) {
        if (!authToken.validate()) {
            throw new CustomException("토큰이 유효하지 않습니다.");
        }

        Claims claims = authToken.getTokenClaims();
        if (claims == null) {
            throw new CustomException("JWT 클레임이 없습니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
    }
}