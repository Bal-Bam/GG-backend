package com.gg.bal_bam.domain.user;

import com.gg.bal_bam.domain.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // 기존 데이터 정리
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll(); // 테스트 후 데이터 정리
    }

    @Transactional
    @Test
    void testExistsByEmail() {
        // Given
        String uniqueEmail = "test" + UUID.randomUUID() + "@example.com";
        User user = User.createUser(uniqueEmail, "password123", "uniqueUser");
        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByEmail(uniqueEmail);

        // Then
        assertTrue(exists);
    }

    @Transactional
    @Test
    void testValidateUsername() {
        // Given
        String uniqueUsername = "user" + UUID.randomUUID();
        User user = User.createUser("test@example.com", "password123", uniqueUsername);
        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByUsername(uniqueUsername);

        // Then
        assertTrue(exists);
    }
}