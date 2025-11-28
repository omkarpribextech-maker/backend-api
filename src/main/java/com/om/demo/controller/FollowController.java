package com.om.demo.controller;

import com.om.demo.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class FollowController {

    @Autowired FollowService followService;

    @PostMapping("/{userId}/follow")
    public ResponseEntity<?> follow(@PathVariable Long userId, @RequestParam Long targetUser) {
        return ResponseEntity.ok(followService.follow(userId, targetUser));
    }

    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<?> unfollow(@PathVariable Long userId, @RequestParam Long targetUser) {
        return ResponseEntity.ok(followService.unfollow(userId, targetUser));
    }
}
