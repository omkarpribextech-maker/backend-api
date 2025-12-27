package com.pribex.demo.service;

import com.pribex.demo.dto.VerifyOtpResponse;
import com.pribex.demo.model.Otp;
import com.pribex.demo.model.User;
import com.pribex.demo.repository.OtpRepository;
import com.pribex.demo.repository.UserRepository;
import com.pribex.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepo;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    // Generate & send OTP
    public void generateAndSendOtp(String identifier, String purpose) {

        String otpCode = String.valueOf(new Random().nextInt(9000) + 1000); // 4 digit

        Otp otp = new Otp();
        otp.setIdentifier(identifier);
        otp.setOtp(otpCode);
        otp.setPurpose(purpose);
        otp.setExpiry(LocalDateTime.now().plusMinutes(5));

        otpRepo.save(otp);

        // TODO: email/SMS send — for now just print
        System.out.println("OTP for " + identifier + " = " + otpCode);
    }

    public VerifyOtpResponse verifyOtpForSignup(String identifier, String code) {

        // Fetch latest OTP for SIGNUP
        Otp otp = otpRepo.findTopByIdentifierAndPurposeOrderByIdDesc(identifier, "SIGNUP");

        if (otp == null) {
            return new VerifyOtpResponse(false, "OTP not found", null, null, null);
        }

        if (!otp.getOtp().equals(code)) {
            return new VerifyOtpResponse(false, "Invalid OTP", null, null, null);
        }

        if (otp.getExpiry().isBefore(LocalDateTime.now())) {
            return new VerifyOtpResponse(false, "OTP expired", null, null, null);
        }

        // OTP is valid → fetch user
        User user = userRepo.findByEmail(identifier);
        if (user == null) {
            user = userRepo.findByPhone(identifier);
        }

        if (user == null) {
            return new VerifyOtpResponse(false, "User not found for this identifier", null, null, null);
        }

        // Generate Tokens
        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return new VerifyOtpResponse(
                true,
                "OTP verified successfully",
                user,
                accessToken,
                refreshToken
        );
    }


}
