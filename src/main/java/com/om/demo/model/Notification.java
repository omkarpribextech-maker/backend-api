package com.om.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
@Entity
@Table(name = "notification")
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user; // recipient

    private String type; // LIKE, COMMENT, FOLLOW, STORY_VIEW, ANNOUNCEMENT

    @ManyToOne @JoinColumn(name = "actor_user_id")
    private User actor;

    private Long postId;
    private Long storyId;

    private boolean isRead = false;
    private Instant createdAt = Instant.now();

    @Column(columnDefinition = "text")
    private String payload; // optional JSON string
}
