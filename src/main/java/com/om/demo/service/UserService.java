package com.om.demo.service;

import com.om.demo.dto.UpdateProfileRequest;
import com.om.demo.model.Interest;
import com.om.demo.model.User;
import com.om.demo.repository.InterestRepository;
import com.om.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    InterestRepository interestRepo;

    public String updateProfile(Long userId, UpdateProfileRequest req) {
        User user = userRepo.findById(userId).orElse(null);

        if (user == null) return "User not found";

        user.setFullName(req.fullName);
        user.setPhone(req.phone);
        user.setProfileUpdated(true);

        userRepo.save(user);
        return "Profile updated";
    }

    public boolean isProfileUpdated(Long userId){
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return false;

        return user.isProfileUpdated();
    }

    public String updateLocation(Long userId, Double lat, Double lng){
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return "User not found";

        user.setLatitude(lat);
        user.setLongitude(lng);

        userRepo.save(user);
        return "Location updated";
    }

    public String updateInterests(Long userId, List<Long> interestIds){
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return "User not found";

        List<Interest> interests = interestRepo.findAllById(interestIds);
        user.setInterests(interests);

        userRepo.save(user);
        return "Interests updated";
    }
}

