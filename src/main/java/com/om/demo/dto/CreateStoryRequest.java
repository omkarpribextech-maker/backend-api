package com.om.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CreateStoryRequest {
    private Long userId;
    private String mediaUrl;
    private String mediaType;    // image | video
    private String contentType;  // story

    private Integer width;
    private Integer height;
    private Double aspectRatio;
    private Double duration;

    private Double lat;
    private Double lng;
    private String privacy;       // PUBLIC / FRIENDS / HIDE / ONLY
    private List<Long> hideUserIds;
    private List<Long> onlyUserIds;

}
