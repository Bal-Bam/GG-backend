package com.gg.bal_bam.domain.user.service;

import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.*;
import com.gg.bal_bam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 일반 회원가입
    @Transactional
    @Override
    public void registerUser(String email, String username, String password) {
        validateEmail(email);
        validateUsername(username);
        String encodedPassword = encodePassword(password);
        User user = User.createUser(email, encodedPassword, username);
        userRepository.save(user);
    }

    @Override
    public void validateEmail(String email) {
        boolean emailExists = userRepository.existsByEmail(email);
        if (emailExists) {
            throw new CustomException("이미 사용 중인 이메일입니다.");
        }
    }

    @Override
    public void validateUsername(String username) {
        boolean usernameExists = userRepository.existsByUsername(username);
        if (usernameExists) {
            throw new CustomException("이미 사용 중인 사용자 이름입니다.");
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
