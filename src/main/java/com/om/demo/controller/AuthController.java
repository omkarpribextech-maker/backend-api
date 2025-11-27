package com.om.demo.controller;

import com.om.demo.dto.SignInRequest;
import com.om.demo.dto.SignUpRequest;
import com.om.demo.service.AuthService;
import com.om.demo.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private OtpService otpService;

    // ---------------- SIGN IN ----------------
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest req) {
        return ResponseEntity.ok(Map.of("message", authService.signIn(req)));
    }

    // ---------------- SIGNUP FLOW ----------------
    @PostMapping("/signup/request-otp")
    public ResponseEntity<?> signupRequestOtp(@RequestParam String identifier) {
        otpService.generateAndSendOtp(identifier, "SIGNUP");
        return ResponseEntity.ok(Map.of("message", "OTP sent"));
    }

    @PostMapping("/signup/verify-otp")
    public ResponseEntity<?> signupVerifyOtp(
            @RequestParam String identifier,
            @RequestParam String code) {

        boolean ok = otpService.verifyOtp(identifier, code, "SIGNUP");
        if (!ok)
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired OTP"));

        return ResponseEntity.ok(Map.of("message", "OTP verified"));
    }

    @PostMapping("/signup/complete")
    public ResponseEntity<?> signupComplete(@Valid @RequestBody SignUpRequest req) {
        return ResponseEntity.ok(Map.of("message", authService.completeSignUp(req)));
    }

    // ---------------- FORGOT PASSWORD FLOW ----------------
    @PostMapping("/forget/request-otp")
    public ResponseEntity<?> forgetRequestOtp(@RequestParam String identifier) {
        otpService.generateAndSendOtp(identifier, "FORGOT");
        return ResponseEntity.ok(Map.of("message", "OTP sent"));
    }

    @PostMapping("/forget/verify-otp")
    public ResponseEntity<?> forgetVerifyOtp(
            @RequestParam String identifier,
            @RequestParam String code) {

        boolean ok = otpService.verifyOtp(identifier, code, "FORGOT");
        if (!ok)
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired OTP"));

        return ResponseEntity.ok(Map.of("message", "OTP verified"));
    }

    @PostMapping("/forget/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String identifier,
            @RequestParam String newPassword) {

        String msg = authService.resetPassword(identifier, newPassword);
        return ResponseEntity.ok(Map.of("message", msg));
    }
}
