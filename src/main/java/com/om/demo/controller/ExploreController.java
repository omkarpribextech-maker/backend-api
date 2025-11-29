package com.om.demo.controller;

import com.om.demo.service.ExploreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/explore")
public class ExploreController {

    @Autowired
    private ExploreService exploreService;

    // SEARCH (Users + Posts)
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String q) {
        return ResponseEntity.ok(exploreService.search(q));
    }

    // TRENDING POSTS
    @GetMapping("/trending")
    public ResponseEntity<?> trending() {
        return ResponseEntity.ok(exploreService.trending());
    }

    // RECOMMENDED POSTS
    @GetMapping("/recommended")
    public ResponseEntity<?> recommended(@RequestParam Long userId) {
        return ResponseEntity.ok(exploreService.recommended(userId));
    }

    // SEARCH USERS ONLY
    @GetMapping("/users")
    public ResponseEntity<?> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(exploreService.searchUsers(query));
    }
}
