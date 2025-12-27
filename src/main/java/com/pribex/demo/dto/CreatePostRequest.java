package com.pribex.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CreatePostRequest {
    public Long userId;
    public String caption;
    public List<String> mediaUrls;
    public Double lat;
    public Double lng;
    public String pincode;
}
