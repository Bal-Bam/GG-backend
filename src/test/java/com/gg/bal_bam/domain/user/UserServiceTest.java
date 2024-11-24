package com.gg.bal_bam.domain.user;

import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.domain.user.service.UserServiceImpl;
import com.gg.bal_bam.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateEmail_whenEmailExists_throwsException() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.validateEmail(email))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 사용 중인 이메일입니다.");

        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void validateEmail_whenEmailDoesNotExist_doesNotThrowException() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // Act & Assert
        assertThatCode(() -> userService.validateEmail(email)).doesNotThrowAnyException();

        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void validateUsername_whenUsernameExists_throwsException() {
        // Arrange
        String username = "testUser";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.validateUsername(username))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 사용 중인 사용자 이름입니다.");

        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void validateUsername_whenUsernameDoesNotExist_doesNotThrowException() {
        // Arrange
        String username = "testUser";
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // Act & Assert
        assertThatCode(() -> userService.validateUsername(username)).doesNotThrowAnyException();

        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void registerUser_successfulRegistration_savesUser() {
        // Arrange
        String email = "test@example.com";
        String username = "testUser";
        String password = "password123";
        String encodedPassword = "encodedPassword123";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        // Act
        userService.registerUser(email, username, password);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
        assertThat(savedUser.getUsername()).isEqualTo(username);
    }

    @Test
    void registerUser_whenEmailExists_throwsException() {
        // Arrange
        String email = "test@example.com";
        String username = "testUser";
        String password = "password123";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.registerUser(email, username, password))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 사용 중인 이메일입니다.");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_whenUsernameExists_throwsException() {
        // Arrange
        String email = "test@example.com";
        String username = "testUser";
        String password = "password123";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.registerUser(email, username, password))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 사용 중인 사용자 이름입니다.");

        verify(userRepository, never()).save(any(User.class));
    }
}