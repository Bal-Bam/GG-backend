package com.gg.bal_bam.domain.user.controller;

import com.gg.bal_bam.common.ResponseTemplate;
import com.gg.bal_bam.domain.user.dto.*;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.domain.user.service.UserService;
import com.gg.bal_bam.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/check-email")
    public ResponseEntity<ResponseTemplate<Void>> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        System.out.println("Received email: " + email);

        try {
            userService.validateEmail(email);
            System.out.println("Email validation passed.");
            return ResponseEntity.ok(ResponseTemplate.ok(null, "사용 가능한 이메일입니다."));
        } catch (CustomException e) {
            System.out.println("CustomException caught: " + e.getMessage());
            // 실패 응답과 409 상태 코드 반환
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseTemplate.fail(null, e.getMessage()));
        } catch (Exception e) {
            System.out.println("Unexpected Exception caught: " + e.getMessage());
            // 내부 서버 오류 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseTemplate.fail(null, "서버에서 문제가 발생했습니다."));
        }
    }

    @PostMapping("/check-username")
    public ResponseEntity<ResponseTemplate<Void>> checkUsername(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        System.out.println(username);
        try {
            userService.validateUsername(username);
            return ResponseEntity.ok(ResponseTemplate.ok(null, "Username is available."));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseTemplate.fail(null, e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseTemplate<Void> signupUser(@RequestBody SignupRequest request) {
        String email = request.getEmail();
        String username = request.getUsername();
        String password = request.getPassword();

        userService.registerUser(email, username, password);
        return ResponseTemplate.ok(null, "회원가입이 완료되었습니다.");
    }

}
