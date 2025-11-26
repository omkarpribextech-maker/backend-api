package com.om.demo.controller;

import com.om.demo.dto.ResetPasswordRequest;
import com.om.demo.dto.SignInRequest;
import com.om.demo.dto.SignUpRequest;
import com.om.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signup")
    public String signUp(@RequestBody SignUpRequest req){
        return authService.signUp(req);
    }

    @PostMapping("/signin")
    public String signIn(@RequestBody SignInRequest req){
        return authService.signIn(req);
    }

    // GET to request a token (returns token here). Use ?email=...
    @GetMapping("/forget")
    public String forget(@RequestParam String email){
        return authService.forgetPassword(email);
    }

    // POST to reset password using the token generated above
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest req){
        return authService.resetPassword(req);
    }
}
