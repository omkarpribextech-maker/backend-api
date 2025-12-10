package com.om.demo.dto;

import lombok.Data;
import com.om.demo.model.User;

import java.time.Instant;

@Data
public class StoryResponse {
    private Long id;
    private String mediaUrl;
    private String mediaType;
    private String contentType;

    private Integer width;
    private Integer height;
    private Double aspectRatio;
    private Double duration;

    private Double locationLat;
    private Double locationLng;

    private Instant createdAt;
    private Instant expiresAt;

    private Integer viewsCount;

    private User user;
}
