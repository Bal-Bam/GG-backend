package com.gg.bal_bam.domain.user;

import com.gg.bal_bam.domain.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 각 테스트 후 데이터 롤백
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll(); // 데이터 초기화
    }

    @Test
    public void testValidateUsername() {
        // Given
        User user = User.createUser("test@example.com", "password", "testUser");
        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByUsername("testUser");

        // Then
        Assertions.assertTrue(exists, "testUser라는 사용자 이름이 존재해야 합니다.");
    }

    @Test
    public void testUsernameNotExists() {
        // When
        boolean exists = userRepository.existsByUsername("nonexistentUser");

        // Then
        Assertions.assertFalse(exists, "nonexistentUser라는 사용자 이름이 존재하지 않아야 합니다.");
    }

    @Test
    public void testExistsByEmail() {
        // Given
        String testEmail = "existing@example.com";
        User user = User.createUser(testEmail, "password123", "testUser");
        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByEmail(testEmail);

        // Then
        Assertions.assertTrue(exists, "existing@example.com 이메일이 존재해야 합니다.");
    }

    @Test
    public void testEmailNotExists() {
        // When
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        Assertions.assertFalse(exists, "nonexistent@example.com 이메일이 존재하지 않아야 합니다.");
    }
}