package com.pribex.demo.dto;

import lombok.Data;

@Data
public class VideoChunkInfo {
    private String originalVideoId;
    private Integer chunkIndex;
    private Integer totalChunks;
    private Long startMs;
    private Long endMs;
}
