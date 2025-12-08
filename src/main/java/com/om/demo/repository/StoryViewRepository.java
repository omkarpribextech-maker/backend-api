package com.om.demo.repository;

import com.om.demo.model.StoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoryViewRepository extends JpaRepository<StoryView, Long> {
    Optional<StoryView> findByStoryIdAndViewerId(Long storyId, Long viewerId);
    int countByStoryId(Long storyId);
    @Modifying
    @Query("DELETE FROM StoryView sv WHERE sv.story.id = :storyId")
    void deleteAllByStoryId(@Param("storyId") Long storyId);

}

