package com.om.demo.controller;

import com.om.demo.dto.UpdateProfileRequest;
import com.om.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/update-profile/{id}")
    public String updateProfile(@PathVariable Long id,
                                @RequestBody UpdateProfileRequest req){
        return userService.updateProfile(id, req);
    }

    @GetMapping("/is-profile-updated/{id}")
    public boolean check(@PathVariable Long id){
        return userService.isProfileUpdated(id);
    }

    @PostMapping("/update-location/{id}")
    public String updateLocation(@PathVariable Long id,
                                 @RequestParam Double lat,
                                 @RequestParam Double lng){
        return userService.updateLocation(id, lat, lng);
    }

    @PostMapping("/interests/{id}")
    public String updateInterests(@PathVariable Long id,
                                  @RequestBody List<Long> interestIds){
        return userService.updateInterests(id, interestIds);
    }
}
