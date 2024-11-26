package com.gg.bal_bam.domain.user.service;

import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.dto.LoginRequest;
import com.gg.bal_bam.domain.user.dto.LoginResponse;
import com.gg.bal_bam.domain.user.dto.UserResponse;
import com.gg.bal_bam.domain.user.model.*;
import com.gg.bal_bam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // 일반 로그인
    @Override
    public LoginResponse login(LoginRequest request) {
        // 사용자 조회
        User user = isEmail(request.getIdentifier())
                ? userRepository.findByEmail(request.getIdentifier()).orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."))
                : userRepository.findByUsername(request.getIdentifier()).orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException("비밀번호가 일치하지 않습니다.");
        }

        return LoginResponse.of(user);
    }

    public void validateEmail(String email) {
        boolean emailExists = userRepository.existsByEmail(email);
        if (emailExists) {
            throw new CustomException("이미 사용 중인 이메일입니다.");
        }
    }

    public void validateUsername(String username) {
        boolean usernameExists = userRepository.existsByUsername(username);
        if (usernameExists) {
            throw new CustomException("이미 사용 중인 사용자 이름입니다.");
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private boolean isEmail(String input) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return input.matches(emailRegex);
    }
}
