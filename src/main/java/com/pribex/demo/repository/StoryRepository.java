package com.pribex.demo.repository;

import com.pribex.demo.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {

    // Get active stories
    List<Story> findByExpiresAtAfterOrderByCreatedAtDesc(Instant now);


    List<Story> findByExpiresAtBefore(Instant now);
}
