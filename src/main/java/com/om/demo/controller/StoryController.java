package com.om.demo.controller;

import com.om.demo.dto.CreateStoryRequest;
import com.om.demo.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired StoryService storyService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createStory(
            @RequestParam Long userId,
            @RequestParam("media") MultipartFile media,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng
    ) throws Exception {
        return ResponseEntity.ok(
                storyService.createStory(userId, media, lat, lng)
        );
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
