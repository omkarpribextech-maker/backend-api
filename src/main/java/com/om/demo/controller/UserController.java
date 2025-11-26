package com.om.demo.controller;

import com.om.demo.dto.*;
import com.om.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    // Update profile: fullName, phone
    @PutMapping("/{userId}/profile")
    public String updateProfile(@PathVariable Long userId,
                                @RequestBody UpdateProfileRequest req) {
        return userService.updateProfile(userId, req);
    }

    // Check if profile updated
    @GetMapping("/{userId}/isProfileUpdated")
    public boolean isProfileUpdated(@PathVariable Long userId) {
        return userService.isProfileUpdated(userId);
    }

    // Update location
    @PutMapping("/{userId}/location")
    public String updateLocation(@PathVariable Long userId,
                                 @RequestBody LocationUpdateRequest req) {
        return userService.updateLocation(userId, req.lat, req.lng);
    }

    // Update interest categories by IDs
    @PutMapping("/{userId}/interests")
    public String updateInterests(@PathVariable Long userId,
                                  @RequestBody InterestsRequest req) {
        return userService.updateInterests(userId, req.interestIds);
    }

    // Get basic user (optional - for quick check)
    @GetMapping("/{userId}")
    public Object getUser(@PathVariable Long userId) {
        return userService.getUserForResponse(userId);
    }
}
