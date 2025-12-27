package com.pribex.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
@Entity
@Table(name = "follow",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id","followee_id"}))
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne @JoinColumn(name = "followee_id")
    private User followee;

    private Instant createdAt = Instant.now();
}
