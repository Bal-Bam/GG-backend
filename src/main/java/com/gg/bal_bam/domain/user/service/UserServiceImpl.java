package com.gg.bal_bam.domain.user.service;

import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.*;
import com.gg.bal_bam.exception.CustomException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void validateEmail(String email) {
        boolean emailExists = userRepository.existsByEmail(email);
        System.out.println(emailExists);
        if (emailExists) {
            throw new CustomException("이미 사용 중인 이메일입니다.");
        }
    }

    public void validateUsername(String username) {
        boolean usernameExists = userRepository.existsByUsername(username);
        System.out.println(usernameExists);
        if (usernameExists) {
            throw new CustomException("이미 사용 중인 사용자 이름입니다.");
        }
    }

    @Override
    public void registerUser(String email, String username, String password) {
        validateEmail(email);
        validateUsername(username);
        String encodedPassword = encodePassword(password);
        User user = User.createUser(email, encodedPassword, username);
        userRepository.save(user);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
