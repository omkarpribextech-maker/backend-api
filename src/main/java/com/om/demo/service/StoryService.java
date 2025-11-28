package com.om.demo.service;

import com.om.demo.model.*;
import com.om.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StoryService {

    @Autowired StoryRepository storyRepo;
    @Autowired StoryViewRepository viewRepo;
    @Autowired NotificationRepository notificationRepo;
    @Autowired UserRepository userRepo;

    public Story createStory(Long userId, String mediaUrl, Double lat, Double lng) {
        Story s = new Story();
        s.setUser(userRepo.findById(userId).orElseThrow());
        s.setMediaUrl(mediaUrl);
        s.setExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));
        s.setLocationLat(lat);
        s.setLocationLng(lng);
        return storyRepo.save(s);
    }

    public Map<String,Object> viewStory(Long storyId, Long viewerUserId) {
        Optional<StoryView> ex = viewRepo.findByStoryIdAndViewerId(storyId, viewerUserId);
        Story story = storyRepo.findById(storyId).orElseThrow();
        if (ex.isEmpty()) {
            StoryView sv = new StoryView();
            sv.setStory(story);
            sv.setViewer(userRepo.findById(viewerUserId).orElseThrow());
            viewRepo.save(sv);
            story.setViewsCount(story.getViewsCount()+1);
            storyRepo.save(story);

            if (!story.getUser().getId().equals(viewerUserId)) {
                Notification n = new Notification();
                n.setUser(story.getUser());
                n.setType("STORY_VIEW");
                n.setActor(userRepo.findById(viewerUserId).orElseThrow());
                n.setStoryId(storyId);
                notificationRepo.save(n);
            }
        }
        int viewers = viewRepo.countByStoryId(storyId);
        return Map.of("views", viewers, "viewsCount", story.getViewsCount());
    }

    public List<Story> getActiveStories() {
        return storyRepo.findByExpiresAtAfterOrderByCreatedAtDesc(Instant.now());
    }
}
