package com.pribex.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentRequest {
    public Long userId;
    public String text;
}
