package com.om.demo.service;

import com.om.demo.dto.*;
import com.om.demo.model.User;
import com.om.demo.repository.UserRepository;
import com.om.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
    public SignInResponse signIn(SignInRequest req) {

        User user;

        if (req.getIdentifier().contains("@")) {
            user = userRepo.findByEmail(req.getIdentifier());
        } else {
            user = userRepo.findByPhone(req.getIdentifier());
        }


        if (user == null)
            return new SignInResponse(null, null, "User not found");

        if (!user.getPassword().equals(req.getPassword()))
            return new SignInResponse(null, null, "Invalid password");

        String token = jwtUtil.generateToken(String.valueOf(user.getId()));

        return new SignInResponse(
                token,
                user.getId(),
                "Login successful"
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
