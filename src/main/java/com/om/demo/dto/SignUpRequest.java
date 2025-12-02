package com.om.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SignUpRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    public String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone must be 10 digits")
    public String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    public String password;

    @NotBlank(message = "Confirm Password is required")
    public String confirmPassword;

    @NotNull(message = "isAdult is required")
    public Boolean isAdult;

}