package com.om.demo.controller;

import com.om.demo.dto.InterestsRequest;
import com.om.demo.dto.InterestsResponse;
import com.om.demo.model.Interest;
import com.om.demo.model.User;
import com.om.demo.repository.InterestRepository;
import com.om.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
public class InterestController {

    @Autowired
    private InterestRepository interestRepo;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Interest> listAll() {
        return interestRepo.findAll();
    }

    @PostMapping
    public Interest create(@RequestBody Interest interest) {
        return interestRepo.save(interest);
    }

    // Update interest categories by IDs
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateInterests(
            @PathVariable Long userId,
            @RequestBody InterestsRequest req) {

        InterestsResponse response = userService.updateUserInterests(userId, req);

        return ResponseEntity.ok(response);
    }
}
