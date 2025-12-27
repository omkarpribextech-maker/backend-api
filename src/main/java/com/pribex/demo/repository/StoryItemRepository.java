package com.pribex.demo.repository;

import com.pribex.demo.model.StoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryItemRepository extends JpaRepository<StoryItem, Long> {
}
