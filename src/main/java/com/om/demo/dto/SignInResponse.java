package com.om.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponse {
    private String token;
    private Long userId;
    private String message;
}
