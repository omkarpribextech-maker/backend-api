package com.om.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateStoryRequest {
    public Long userId;
    public String mediaUrl;
    public Double lat;
    public Double lng;
}
