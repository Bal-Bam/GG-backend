package com.gg.bal_bam.domain.auth;

import com.gg.bal_bam.domain.auth.controller.AuthController;
import com.gg.bal_bam.domain.auth.dto.LoginRequest;
import com.gg.bal_bam.domain.auth.dto.LoginResponse;
import com.gg.bal_bam.domain.auth.service.AuthService;
import com.gg.bal_bam.domain.user.dto.UserResponse;
import com.gg.bal_bam.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
//@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Security 필터 무시
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    @WithMockUser
    void login_success() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("testUser", "password123");
        LoginResponse loginResponse = LoginResponse.of(
                UserResponse.of(
                        UUID.randomUUID(),
                        "test@example.com",
                        "testUser",
                        null,
                        null,
                        0,
                        false
                ),
                "mockAccessToken"
        );

        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // When
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"identifier\":\"testUser\",\"password\":\"password123\"}")
                .with(csrf()));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.results.user.username").value("testUser"))
                .andExpect(jsonPath("$.results.user.email").value("test@example.com"))
                .andExpect(jsonPath("$.results.accessToken").value("mockAccessToken"));
    }

    @Test
    void login_invalidPassword() throws Exception {
        // Given: Mock AuthService
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new CustomException("비밀번호가 일치하지 않습니다."));

        // When
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"identifier\":\"testUser\",\"password\":\"wrongPassword\"}"));

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

    @Test
    @WithMockUser
    void login_userNotFound() throws Exception {
        // Given
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new CustomException("사용자를 찾을 수 없습니다."));

        // When
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"identifier\":\"nonExistentUser\",\"password\":\"password123\"}"));

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."));
    }

}