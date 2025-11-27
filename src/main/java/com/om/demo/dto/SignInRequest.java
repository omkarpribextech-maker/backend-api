package com.om.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class SignInRequest {

    @NotBlank
    public String identifier;  // email OR phone

    @NotBlank
    public String password;
}
