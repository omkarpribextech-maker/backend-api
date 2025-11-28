package com.om.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
@Entity
@Table(name = "story")
public class Story {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    private String mediaUrl;
    private String type; // IMAGE/VIDEO

    private Instant createdAt = Instant.now();
    private Instant expiresAt;

    private Double locationLat;
    private Double locationLng;

    private int viewsCount = 0;
}
