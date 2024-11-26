package com.gg.bal_bam.domain.auth.token;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

    @Getter
    private final String token;
    private final Key key;

    private static final String AUTHORITIES_KEY = "role";
    private static final String DEFAULT_ROLE = "member"; // 일반 회원

    //일반 사용자의 토큰 생성(회원가입)
    AuthToken(String userId, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(userId, DEFAULT_ROLE, expiry);
    }

    // admin 만들 때 등...
    AuthToken(String userId, String role, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(userId, role, expiry);
    }

    private String createAuthToken(String userId, String role, Date expiry) {
        return Jwts.builder()
                .setSubject(userId)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    public boolean validate() {
        try {
            Claims claims = this.getTokenClaims();
            return claims != null && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return null;
    }
}