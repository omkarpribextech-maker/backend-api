package com.pribex.demo.controller;

import com.pribex.demo.service.ReelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/reels")
public class ReelController {

    @Autowired
    ReelService reelService;

    @PostMapping(
            value = "/upload",
            consumes = {"multipart/form-data"}
    )
    public ResponseEntity<?> upload(
            @RequestParam("userId") Long userId,
            @RequestParam("video") MultipartFile video,
            @RequestParam(value = "caption", required = false) String caption
    ) {
        return ResponseEntity.ok(reelService.uploadReel(userId, video, caption));
    }



    @PostMapping("/{reelId}/like")
    public ResponseEntity<?> like(
            @PathVariable Long reelId,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(reelService.likeReel(reelId, userId));
    }

    @PostMapping("/{reelId}/view")
    public ResponseEntity<?> view(@PathVariable Long reelId) {
        return ResponseEntity.ok(reelService.viewReel(reelId));
    }

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(reelService.listReels());
    }
}

