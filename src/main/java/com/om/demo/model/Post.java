package com.om.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "post")
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "text")
    private String caption;

    private String type; // IMAGE / VIDEO

    private Double locationLat;
    private Double locationLng;

    private String pincode;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    private int likesCount = 0;
    private int commentsCount = 0;
    private int sharesCount = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostMedia> media;
}
