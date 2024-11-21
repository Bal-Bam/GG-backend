package com.gg.bal_bam.domain.user;

import com.gg.bal_bam.domain.user.service.UserService;
import com.gg.bal_bam.domain.user.service.UserServiceImpl;
import com.gg.bal_bam.exception.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    public void testValidateUsernameAvailable() {
        // Given
        when(userRepository.existsByUsername("testUser")).thenReturn(false);

        // When & Then
        Assertions.assertDoesNotThrow(() -> userService.validateUsername("testUser"));
    }

    @Test
    public void testValidateUsernameTaken() {
        // Given
        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        // When & Then
        CustomException exception = Assertions.assertThrows(
                CustomException.class,
                () -> userService.validateUsername("testUser")
        );
        Assertions.assertEquals("이미 사용 중인 사용자 이름입니다.", exception.getMessage());
    }
}