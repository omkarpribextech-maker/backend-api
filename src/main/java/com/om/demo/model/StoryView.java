package com.om.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
@Entity
@Table(name = "story_view",
        uniqueConstraints = @UniqueConstraint(columnNames = {"story_id","viewer_user_id"}))
public class StoryView {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "story_id")
    private Story story;

    @ManyToOne @JoinColumn(name = "viewer_user_id")
    private User viewer;

    private Instant viewedAt = Instant.now();
}
