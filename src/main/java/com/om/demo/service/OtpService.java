package com.om.demo.service;

import com.om.demo.model.Otp;
import com.om.demo.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepo;

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

    // Verify OTP
    public boolean verifyOtp(String identifier, String code, String purpose) {

        Otp otp = otpRepo.findTopByIdentifierAndPurposeOrderByIdDesc(identifier, purpose);

        if (otp == null) return false;

        if (!otp.getOtp().equals(code)) return false;

        if (otp.getExpiry().isBefore(LocalDateTime.now())) return false;

        return true;
    }
}
