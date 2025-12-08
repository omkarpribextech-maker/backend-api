package com.om.demo.repository;

import com.om.demo.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {

    // Get active stories
    List<Story> findByExpiresAtAfterOrderByCreatedAtDesc(Instant now);

    // Count stories created by user in last 24 hours
    int countByUserIdAndCreatedAtAfter(Long userId, Instant after);

    // Find expired stories to auto-delete
    List<Story> findByExpiresAtBefore(Instant now);
}
