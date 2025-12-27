package com.pribex.demo.dto;

import com.pribex.demo.enums.StoryItemType;
import com.pribex.demo.model.MediaEdits;
import com.pribex.demo.model.MusicInfo;
import lombok.Data;

import java.util.List;

@Data
public class CreateStoryItemRequest {

    private String itemId;
    private StoryItemType itemType;
    private String mediaUrl;
    private Integer durationSeconds;
    private Integer orderIndex;

    // video chunk
    private VideoChunkInfo chunkInfo;

    private MediaEdits edits;
    private MusicInfo music;
    private List<TextOverlay> textOverlays;
}

