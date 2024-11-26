package com.gg.bal_bam.domain.user.controller;

import com.gg.bal_bam.common.ResponseTemplate;
import com.gg.bal_bam.domain.user.dto.*;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.domain.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/check-email")
    public ResponseTemplate<Void> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        userService.validateEmail(email);
        return ResponseTemplate.ok(null, "사용 가능한 이메일입니다.");
    }

    @PostMapping("/check-username")
    public ResponseTemplate<Void>checkUsername(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        userService.validateUsername(username);
        return ResponseTemplate.ok(null, "사용 가능한 사용자 이름입니다.");
    }

    @PostMapping("/signup")
    public ResponseTemplate<Void> signupUser(@RequestBody SignupRequest request) {
        String email = request.getEmail();
        String username = request.getUsername();
        String password = request.getPassword();

        userService.registerUser(email, username, password);
        return ResponseTemplate.ok(null, "회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseTemplate<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = userService.login(request);
        return ResponseTemplate.ok(loginResponse);
    }

}
