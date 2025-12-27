package com.pribex.demo.dto;

import com.pribex.demo.enums.StoryItemType;
import com.pribex.demo.model.MediaEdits;
import com.pribex.demo.model.MusicInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class StoryItemResponse {

    private String itemId;
    private StoryItemType itemType;
    private String mediaUrl;
    private Integer durationSeconds;
    private Integer orderIndex;

    private MediaEdits edits;
    private MusicInfo music;

    private List<TextOverlay> textOverlays;

}

