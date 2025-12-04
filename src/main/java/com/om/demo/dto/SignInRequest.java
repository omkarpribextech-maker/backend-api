package com.om.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class SignInRequest {

    @NotBlank
    private String identifier;

    @NotBlank
    private String password;

    public String getIdentifier() {
        return identifier;
    }

    public String getPassword() {
        return password;
    }
}
