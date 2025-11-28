package com.om.demo.controller;

import com.om.demo.dto.CommentRequest;
import com.om.demo.dto.CreatePostRequest;
import com.om.demo.model.Post;
import com.om.demo.model.PostComment;
import com.om.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest req) {
        Post p = postService.createPost(req);
        return ResponseEntity.ok(p);
    }

    @GetMapping("/feed")
    public ResponseEntity<?> feed(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(postService.getFeed(PageRequest.of(page, size)));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> like(@PathVariable Long postId, @RequestParam Long userId) {
        return ResponseEntity.ok(postService.toggleLike(postId, userId));
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<?> comment(@PathVariable Long postId, @RequestBody CommentRequest req) {
        PostComment c = postService.addComment(postId, req.getUserId(), req.getText());
        return ResponseEntity.ok(c);
    }

    @PostMapping("/{postId}/bookmark")
    public ResponseEntity<?> bookmark(@PathVariable Long postId, @RequestParam Long userId) {
        return ResponseEntity.ok(postService.toggleBookmark(postId, userId));
    }

    @PostMapping("/{postId}/share")
    public ResponseEntity<?> share(@PathVariable Long postId,
                                   @RequestParam Long userId,
                                   @RequestParam String sharedTo,
                                   @RequestParam(required = false) Long sharedToId) {
        return ResponseEntity.ok(postService.sharePost(postId, userId, sharedTo, sharedToId));
    }
}
