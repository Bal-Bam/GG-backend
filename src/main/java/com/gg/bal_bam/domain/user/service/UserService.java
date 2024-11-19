package com.gg.bal_bam.domain.user.service;

import com.gg.bal_bam.domain.user.model.AuthProvider;
import com.gg.bal_bam.domain.user.model.User;

public interface UserService {
    void registerUser(String email, String username, String password);

}