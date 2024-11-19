package com.gg.bal_bam.domain.user.service;

import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.*;
import com.gg.bal_bam.exception.CustomException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(String email, String username, String password) {
        String encodedPassword = encodePassword(password);
        User user = User.createUser(email, encodedPassword, username);
        userRepository.save(user);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}