package com.pribex.demo.controller;

import com.pribex.demo.dto.CreateStoryRequest;
import com.pribex.demo.dto.StoryResponse;
import com.pribex.demo.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    StoryService storyService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StoryResponse> createStory(
            @RequestBody CreateStoryRequest request,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return ResponseEntity.ok(storyService.createStory(request, userId));
    }


    @PostMapping("/{storyId}/view")
    public ResponseEntity<?> view(@PathVariable Long storyId,
                                  @RequestParam Long userId) {
        return ResponseEntity.ok(storyService.viewStory(storyId, userId));
    }



    @GetMapping("/active")
    public ResponseEntity<?> getActive(@RequestParam Long userId) {
        return ResponseEntity.ok(storyService.getActiveStories(userId));
    }

}
