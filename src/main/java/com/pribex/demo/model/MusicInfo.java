package com.pribex.demo.model;

import lombok.Data;

@Data
public class MusicInfo {
    private String musicId;
    private String title;
    private String artist;
    private String audioUrl;
    private Long trimStartMs;
    private Long trimEndMs;
    private Float musicVolume;
    private Float clipVolume;
}

