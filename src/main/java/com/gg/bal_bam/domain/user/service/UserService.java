package com.gg.bal_bam.domain.user.service;

public interface UserService {
    void registerUser(String email, String username, String password);
    void validateEmail(String email);
    void validateUsername(String username);
}