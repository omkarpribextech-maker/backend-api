package com.om.demo.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class SignUpRequest {
    @NotBlank public String fullName;
    @Email public String email;
    @NotBlank public String phone;
    @NotBlank public String password;
    @NotBlank public String confirmPassword;
}