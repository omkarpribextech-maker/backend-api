package com.om.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "reel_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"reel_id","user_id"}))
public class ReelLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "reel_id")
    private Reel reel;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    private Instant likedAt = Instant.now();
}

