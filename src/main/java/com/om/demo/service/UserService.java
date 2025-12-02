package com.om.demo.service;

import com.om.demo.dto.*;
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

    public ProfileResponse updateProfile(Long userId, UpdateProfileRequest req) {

        User user = userRepo.findById(userId).orElse(null);

        if (user == null) {
            return new ProfileResponse(false, "User not found", null);
        }

        if (req.firstName != null) user.setFirstName(req.firstName);
        if (req.lastName != null) user.setLastName(req.lastName);
        if (req.username != null) user.setUsername(req.username);
        if (req.phone != null) user.setPhone(req.phone);

        user.setProfileUpdated(true);

        User updatedUser = userRepo.save(user);

        return new ProfileResponse(
                true,
                "Profile updated successfully",
                updatedUser
        );
    }


    public boolean isProfileUpdated(Long userId){
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) return false;

        return user.isProfileUpdated();
    }

    public LocationResponse updateLocation(Long userId, LocationUpdateRequest req) {

        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return new LocationResponse(false, "User not found", null);
        }

        // Update coordinates
        user.setLatitude(req.lat);
        user.setLongitude(req.lng);

        // Optional pincode
        user.setPincode(req.pincode);

        // New location fields
        user.setCountry(req.country);
        user.setState(req.state);
        user.setDistrict(req.district);
        user.setCity(req.city);
        user.setArea(req.area);

        User updatedUser = userRepo.save(user);

        return new LocationResponse(
                true,
                "Location updated successfully",
                updatedUser
        );
    }


    public InterestsResponse updateUserInterests(Long userId, InterestsRequest req) {

        User user = userRepo.findById(userId).orElse(null);

        if (user == null) {
            return new InterestsResponse(false, "User not found", null);
        }

        // Fetch all interests by IDs
        List<Interest> interests = interestRepo.findAllById(req.getInterestIds());

        // Update user's interests
        user.setInterests(interests);

        User updatedUser = userRepo.save(user);

        return new InterestsResponse(
                true,
                "Interests updated successfully",
                updatedUser
        );
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
