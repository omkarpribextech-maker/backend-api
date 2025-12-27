package com.pribex.demo.controller;

import com.pribex.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/are-friends")
    public boolean areFriends(@RequestParam Long u1, @RequestParam Long u2) {
        return userRepo.areFriends(u1, u2);
    }
}

