package com.pribex.demo.dto;

import jakarta.validation.constraints.*;

public class SignUpRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    public String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone must be 10 digits")
    public String phone;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must be strong (min 8 chars, 1 uppercase, 1 lowercase, 1 number, 1 special character)"
    )
    public String password;


    @NotBlank(message = "Confirm Password is required")
    public String confirmPassword;

    @NotNull(message = "isAdult is required")
    public Boolean isAdult;

}