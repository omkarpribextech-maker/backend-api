package com.om.demo.repository;

import com.om.demo.model.StoryView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoryViewRepository extends JpaRepository<StoryView, Long> {

    Optional<StoryView> findByStoryIdAndViewerId(Long storyId, Long viewerId);

    int countByStoryId(Long storyId);
}
