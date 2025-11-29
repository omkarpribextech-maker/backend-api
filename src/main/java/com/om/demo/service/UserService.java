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

        user.setFirstName(req.firstName);
        user.setLastName(req.lastName);
        user.setUsername(req.username);
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

    // helper: return user (but avoid sending password in real app)
    public Object getUserForResponse(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return "User not found";

        // create a simple safe response map
        return new java.util.HashMap<String, Object>() {{
            put("id", user.getId());
            put("first name",user.getFirstName());
            put("last name",user.getLastName());
            put("email", user.getEmail());
            put("phone", user.getPhone());
            put("isProfileUpdated", user.isProfileUpdated());
            put("latitude", user.getLatitude());
            put("longitude", user.getLongitude());
            put("interests", user.getInterests());
        }};
    }
    public List<String> suggestUsernames(String firstName, String lastName) {

        String f = firstName.toLowerCase();
        String l = lastName.toLowerCase();

        return List.of(
                f + "_" + l,
                f + "." + l,
                f + l,
                f + "_" + (int)(Math.random()*900 + 100),   // firstname_123
                f + l + (int)(Math.random()*900 + 100),    // firstnamelastname123
                f.substring(0, 1) + l                      // f + lastname
        );
    }
    public User getUserEntity(Long userId) {
        return userRepo.findById(userId).orElse(null);
    }
}
