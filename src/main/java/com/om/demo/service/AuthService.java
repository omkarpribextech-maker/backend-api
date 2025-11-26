package com.om.demo.service;

import com.om.demo.dto.SignInRequest;
import com.om.demo.dto.SignUpRequest;
import com.om.demo.model.PasswordResetToken;
import com.om.demo.model.User;
import com.om.demo.repository.PasswordResetTokenRepository;
import com.om.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordResetTokenRepository tokenRepo;

    public String signUp(SignUpRequest req) {
        if (userRepo.existsByEmail(req.email)) {
            return "Email already exists";
        }

        User user = new User();
        user.setFullName(req.fullName);
        user.setEmail(req.email);
        user.setPassword(req.password);
        user.setPhone(req.phone);

        userRepo.save(user);
        return "User registered successfully";
    }

    public String signIn(SignInRequest req) {
        User user = userRepo.findByEmail(req.email);

        if (user == null) return "User not found";
        if (!user.getPassword().equals(req.password)) return "Invalid password";

        return "Login successful";
    }

    public String forgetPassword(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) return "User not found";

        String token = UUID.randomUUID().toString();

        PasswordResetToken pr = new PasswordResetToken();
        pr.setEmail(email);
        pr.setToken(token);
        pr.setExpiryTime(LocalDateTime.now().plusMinutes(15));

        tokenRepo.save(pr);

        return token;
    }
}
