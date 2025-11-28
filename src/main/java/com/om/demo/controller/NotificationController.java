package com.om.demo.controller;

import com.om.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired NotificationService notificationService;

    @GetMapping("/today")
    public ResponseEntity<?> today(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.getToday(userId));
    }

    @GetMapping("/yesterday")
    public ResponseEntity<?> yesterday(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.getYesterday(userId));
    }
}
