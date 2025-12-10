package com.om.demo.controller;

import com.om.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> all(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.getAll(userId));
    }

    @GetMapping("/unread")
    public ResponseEntity<?> unread(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.getUnread(userId));
    }

    @GetMapping("/today")
    public ResponseEntity<?> today(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.getToday(userId));
    }

    @GetMapping("/yesterday")
    public ResponseEntity<?> yesterday(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.getYesterday(userId));
    }

    @PostMapping("/read/{id}")
    public ResponseEntity<?> markRead(@PathVariable Long id) {
        notificationService.markRead(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/read-all")
    public ResponseEntity<?> markAll(@RequestParam Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(Map.of("success", true));
    }
}

