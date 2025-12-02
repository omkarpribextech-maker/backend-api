package com.om.demo.dto;

import com.om.demo.model.User;

public class SignUpCompleteResponse {

    public boolean success;
    public String message;
    public User user;

    public SignUpCompleteResponse(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }
}
