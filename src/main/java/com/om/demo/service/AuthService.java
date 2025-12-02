package com.om.demo.service;

import com.om.demo.dto.*;
import com.om.demo.model.User;
import com.om.demo.repository.UserRepository;
import com.om.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    JwtUtil jwtUtil;

    // -------- SIGN UP COMPLETE -----------
    public SignUpCompleteResponse completeSignUp(SignUpRequest req) {

        // Business validations
        if (userRepo.existsByEmail(req.email)) {
            return new SignUpCompleteResponse(false, "Email already exists", null);
        }

        if (userRepo.existsByPhone(req.phone)) {
            return new SignUpCompleteResponse(false, "Phone already exists", null);
        }

        // Create new user
        User user = new User();
        user.setEmail(req.email);
        user.setPhone(req.phone);
        user.setPassword(req.password);

        User savedUser = userRepo.save(user);

        return new SignUpCompleteResponse(
                true,
                "Signup completed successfully",
                savedUser
        );
    }

    // -------- SIGN IN -----------

    public Object signIn(SignInRequest req) {

        User user;

        if (req.identifier.contains("@")) {
            user = userRepo.findByEmail(req.identifier);
        } else {
            user = userRepo.findByPhone(req.identifier);
        }

        if (user == null)
            return Map.of("error","User not found");

        if (!user.getPassword().equals(req.password))
            return Map.of("error","Invalid password");

        // ---- token generate ----
        String token = jwtUtil.generateToken(String.valueOf(user.getId()));

        return Map.of(
                "message", "Login successful",
                "token", token,
                "userId", user.getId()
        );
    }



    // -------- RESET PASSWORD -----------
    public String resetPassword(String identifier, String newPassword) {

        User user;
        if (identifier.contains("@")) {
            user = userRepo.findByEmail(identifier);
        } else {
            user = userRepo.findByPhone(identifier);
        }

        if (user == null) return "User not found";

        user.setPassword(newPassword);
        userRepo.save(user);

        return "Password reset successful";
    }
}
