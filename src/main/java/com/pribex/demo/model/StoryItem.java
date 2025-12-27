package com.pribex.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pribex.demo.dto.StoryOverlay;
import com.pribex.demo.dto.TextOverlay;
import com.pribex.demo.enums.StoryItemType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "story_item")
@Getter
@Setter
public class StoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "story_id")
    @JsonIgnore
    private Story story;

    @Enumerated(EnumType.STRING)
    private StoryItemType itemType;

    private String itemId;
    private String mediaUrl;
    private Integer durationSeconds;
    private Integer orderIndex;

    // VIDEO CHUNK INFO
    private String originalVideoId;
    private Integer chunkIndex;
    private Integer totalChunks;
    private Long startMs;
    private Long endMs;

    // JSON DATA
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private MediaEdits edits;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private MusicInfo music;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<TextOverlay> textOverlays;

}
