package com.pribex.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pribex.demo.model.MusicInfo;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TextOverlay {
    private String id;
    private String text;
    private Float positionX;
    private Float positionY;
    private Float scale;
    private Float rotation;
    private TextStyle style;
    private MusicInfo music;
}
