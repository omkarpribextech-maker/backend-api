package com.pribex.demo.model;

import com.pribex.demo.enums.PrivacyType;
import com.pribex.demo.enums.StoryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "story")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    private String mediaUrl;
    private String type; // IMAGE | VIDEO
    private String contentType = "story";

    private Integer width;
    private Integer height;
    private Double aspectRatio;
    private Double duration; // seconds

    private Instant createdAt = Instant.now();
    private Instant expiresAt;


    private String storyId;        // unique id for video-story session
    private Integer chunkIndex;    // video chunk number
    private String parentVideoId;  // main video id (if any)
    private Boolean isLastChunk;   // last chunk?
    private String thumbnailUrl;   // thumbnail image
    private String status;         // active/processing/error


    private Integer fps;
    private Double locationLat;
    private Double locationLng;

    private int viewsCount = 0;


    // For hide list
    @ElementCollection
    private List<Long> hideUserIds;

    // For only / close friends
    @ElementCollection
    private List<Long> onlyUserIds;

    /* overlays (music, text, font, stickers etc.) */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Object overlays;

    @OneToMany(mappedBy = "story", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StoryView> views;



    @Enumerated(EnumType.STRING)
    private StoryType storyType;


    @ElementCollection
    private List<Long> allowedUserIds;

    @ElementCollection
    private List<Long> blockedUserIds;


    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<StoryItem> items;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrivacyType privacy;


}
