package com.pribex.demo.dto;

import com.pribex.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InterestsResponse {
    private boolean success;
    private String message;
    private User user;
}
