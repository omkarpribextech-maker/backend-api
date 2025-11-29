package com.om.demo.service;

import com.om.demo.dto.SignInRequest;
import com.om.demo.dto.SignUpRequest;
import com.om.demo.model.User;
import com.om.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepo;

    // -------- SIGN UP COMPLETE -----------
    public String completeSignUp(SignUpRequest req) {

        if (!req.password.equals(req.confirmPassword)) {
            return "Passwords do not match";
        }

        if (userRepo.existsByEmail(req.email)) return "Email already exists";
        if (userRepo.existsByPhone(req.phone)) return "Phone already exists";

        User user = new User();
        user.setEmail(req.email);
        user.setPhone(req.phone);
        user.setPassword(req.password);

        userRepo.save(user);
        return "Signup successfully completed";
    }

    // -------- SIGN IN -----------
    public String signIn(SignInRequest req) {
        User user;

        if (req.identifier.contains("@")) {
            user = userRepo.findByEmail(req.identifier);
        } else {
            user = userRepo.findByPhone(req.identifier);
        }

        if (user == null) return "User not found";

        if (!user.getPassword().equals(req.password))
            return "Invalid password";

        return "Login successful";
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

    public String updateLocation(Long userId, Double lat, Double lng, String pincode){
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return "User not found";

        user.setLatitude(lat);
        user.setLongitude(lng);
        user.setPincode(pincode);

        userRepo.save(user);
        return "Location updated";
    }

}
