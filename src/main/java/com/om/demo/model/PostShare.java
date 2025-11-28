package com.om.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
@Entity
@Table(name = "post_share")
public class PostShare {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    private String sharedTo; // USER / STORY / EXTERNAL
    private Long sharedToId;

    private Instant createdAt = Instant.now();
}
