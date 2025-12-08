package com.om.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class SignInRequest {

    @NotBlank(message = "Identifier is required")
    private String identifier;

    @NotBlank(message = "Password is required")
    private String password;

    public String getIdentifier() {
        return identifier;
    }

    public String getPassword() {
        return password;
    }
}
