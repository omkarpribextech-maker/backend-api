package com.om.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    private Integer fps;
    private Double locationLat;
    private Double locationLng;

    private int viewsCount = 0;

    // Privacy type: PUBLIC, FRIENDS, HIDE, ONLY
    private String privacy;

    // For hide list
    @ElementCollection
    private List<Long> hideUserIds;

    // For only / close friends
    @ElementCollection
    private List<Long> onlyUserIds;


    @OneToMany(mappedBy = "story", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StoryView> views;

}
