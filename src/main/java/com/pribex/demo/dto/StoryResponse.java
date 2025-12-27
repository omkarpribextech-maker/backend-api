package com.pribex.demo.dto;

import lombok.Data;
import com.pribex.demo.model.User;

import java.time.Instant;
import java.util.List;

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

    private String storyId;
    private Integer chunkIndex;
    private String parentVideoId;
    private Boolean isLastChunk;
    private String thumbnailUrl;
    private String status;

    private User user;

    private Object overlays;
    private List<StoryItemResponse> items;
}
