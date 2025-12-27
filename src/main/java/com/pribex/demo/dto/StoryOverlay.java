package com.pribex.demo.dto;


import lombok.Data;
import java.util.List;

@Data
public class StoryOverlay {

    private MusicOverlay music;
    private List<TextOverlay> texts;

    @Data
    public static class MusicOverlay {
        private String trackId;
        private String title;
        private String artist;
        private Double startTime;
        private Double endTime;
        private Double volume;
    }

}

