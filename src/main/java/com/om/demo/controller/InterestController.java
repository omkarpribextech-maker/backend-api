package com.om.demo.controller;

import com.om.demo.model.Interest;
import com.om.demo.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
public class InterestController {

    @Autowired
    InterestRepository interestRepo;

    @GetMapping
    public List<Interest> listAll() {
        return interestRepo.findAll();
    }

    @PostMapping
    public Interest create(@RequestBody Interest interest) {
        return interestRepo.save(interest);
    }
}
