package com.pribex.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String profilePic;
    private boolean isFollowedByMe;
}
