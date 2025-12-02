package com.om.demo.controller;

import com.om.demo.dto.*;
import com.om.demo.model.User;
import com.om.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping("/{userId}/profile")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
            @RequestBody UpdateProfileRequest req) {

        ProfileResponse response = userService.updateProfile(userId, req);

        return ResponseEntity.ok(response);
    }


    // Check if profile updated
    @GetMapping("/{userId}/isProfileUpdated")
    public boolean isProfileUpdated(@PathVariable Long userId) {
        return userService.isProfileUpdated(userId);
    }

    // Update location
    @PutMapping("/{userId}/location")
    public ResponseEntity<?> updateLocation(
            @PathVariable Long userId,
            @RequestBody LocationUpdateRequest req) {

        LocationResponse response = userService.updateLocation(userId, req);

        return ResponseEntity.ok(response);
    }




    // Get basic user (optional - for quick check)
    @GetMapping("/{userId}")
    public Object getUser(@PathVariable Long userId) {
        return userService.getUserForResponse(userId);
    }
    @GetMapping("/suggest-username")
    public List<String> suggestUsernames(@RequestParam String firstName,
                                         @RequestParam String lastName) {
        return userService.suggestUsernames(firstName, lastName);
    }

}
