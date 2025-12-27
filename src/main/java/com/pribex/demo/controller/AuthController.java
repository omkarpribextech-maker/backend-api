package com.pribex.demo.controller;


import com.pribex.demo.dto.*;
import com.pribex.demo.service.AuthService;
import com.pribex.demo.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private AuthService authService;


    @Autowired
    private OtpService otpService;

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest req){
        return ResponseEntity.ok(authService.signIn(req));
    }


    // Signup OTP flow
    @PostMapping("/signup/request-otp")
    public ResponseEntity<?> signupRequestOtp(@RequestParam String identifier){
        otpService.generateAndSendOtp(identifier, "SIGNUP");
        return ResponseEntity.ok( Map.of(
                "success", true,
                "message", "OTP sent"
        ));
    }


    @PostMapping("/signup/verify-otp")
    public ResponseEntity<?> signupVerifyOtp(@RequestParam String identifier,
                                             @RequestParam String code) {

        VerifyOtpResponse response = otpService.verifyOtpForSignup(identifier, code);

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }



    @PostMapping("/signup/complete")
    public ResponseEntity<?> signupComplete(@Valid @RequestBody SignUpRequest req) {

        // Input validation
        if (!req.password.equals(req.confirmPassword)) {
            return ResponseEntity.badRequest().body(
                    new SignUpCompleteResponse(false, "Passwords do not match", null)
            );
        }

        if (req.isAdult == null || !req.isAdult) {
            return ResponseEntity.badRequest().body(
                    new SignUpCompleteResponse(false, "User must be adult", null)
            );
        }

        // Service will return final response
        SignUpCompleteResponse response = authService.completeSignUp(req);

        return ResponseEntity.ok(response);
    }



    // Forget password OTP flow
    @PostMapping("/forget/request-otp")
    public ResponseEntity<?> forgetRequestOtp(@RequestParam String identifier){
        otpService.generateAndSendOtp(identifier, "FORGOT");
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OTP sent"
        ));
    }


    @PostMapping("/forget/verify-otp")
    public ResponseEntity<?> forgetVerifyOtp(@RequestParam String identifier,
                                             @RequestParam String code) {

        VerifyOtpResponse response = otpService.verifyOtpForSignup(identifier, code);

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/forget/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String identifier,
            @RequestParam String newPassword) {

        String msg = authService.resetPassword(identifier, newPassword);
        return ResponseEntity.ok(Map.of("message", msg));
    }

}