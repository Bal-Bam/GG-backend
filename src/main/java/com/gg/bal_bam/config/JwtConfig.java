package com.gg.bal_bam.config;

import com.gg.bal_bam.domain.auth.token.AuthTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    @Value("${app.auth.tokenExpiry}")
    private long tokenExpiry;

    @Bean
    public AuthTokenProvider jwtProvider() {
        return new AuthTokenProvider(tokenSecret, tokenExpiry);
    }
}