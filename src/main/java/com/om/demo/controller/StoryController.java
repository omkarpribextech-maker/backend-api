package com.om.demo.controller;

import com.om.demo.dto.CreateStoryRequest;
import com.om.demo.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired StoryService storyService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateStoryRequest req) {
        return ResponseEntity.ok(storyService.createStory(req.getUserId(), req.getMediaUrl(), req.getLat(), req.getLng()));
    }

    @PostMapping("/{storyId}/view")
    public ResponseEntity<?> view(@PathVariable Long storyId, @RequestParam Long userId) {
        return ResponseEntity.ok(storyService.viewStory(storyId, userId));
    }

    @GetMapping("/active")
    public ResponseEntity<?> active() {
        return ResponseEntity.ok(storyService.getActiveStories());
    }
}
