package com.pribex.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "reels")
public class Reel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String videoUrl;     // local file path
    private String thumbnailUrl; // optional

    private String caption;

    private Instant createdAt = Instant.now();

    private int likesCount = 0;
    private int viewsCount = 0;


}

