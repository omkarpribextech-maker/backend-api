package com.om.demo.dto;

import com.om.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyOtpResponse {
    private boolean success;
    private String message;
    private User user;
    private String accessToken;
    private String refreshToken;
}
