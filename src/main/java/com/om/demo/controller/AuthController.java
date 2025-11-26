package com.om.demo.controller;

import com.om.demo.dto.SignInRequest;
import com.om.demo.dto.SignUpRequest;
import com.om.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/auth")
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

    @GetMapping("/forget")
    public String forget(@RequestParam String email){
        return authService.forgetPassword(email);
    }
}
