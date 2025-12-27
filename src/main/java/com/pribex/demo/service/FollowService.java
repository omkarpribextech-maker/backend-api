package com.pribex.demo.service;

import com.pribex.demo.dto.FollowResponse;
import com.pribex.demo.model.Follow;
import com.pribex.demo.model.User;
import com.pribex.demo.repository.FollowRepository;
import com.pribex.demo.repository.NotificationRepository;
import com.pribex.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FollowService {

    @Autowired NotificationService notificationService;

    @Autowired FollowRepository followRepo;
    @Autowired UserRepository userRepo;
    @Autowired NotificationRepository notificationRepo;


    // ---------------- FOLLOW ----------------
    public String follow(Long followerId, Long targetUserId) {

        if (followRepo.findByFollowerIdAndFolloweeId(followerId, targetUserId).isPresent()) {
            return "Already following";
        }

        Follow f = new Follow();
        f.setFollower(userRepo.findById(followerId).orElseThrow());
        f.setFollowee(userRepo.findById(targetUserId).orElseThrow());
        followRepo.save(f);

        return "Followed";
    }

    // ---------------- UNFOLLOW ----------------
    public String unfollow(Long followerId, Long targetUserId) {
        var ex = followRepo.findByFollowerIdAndFolloweeId(followerId, targetUserId);
        if (ex.isPresent()) {
            followRepo.delete(ex.get());
            return "Unfollowed";
        }
        return "Not following";
    }

    // ---------------- FOLLOWERS LIST ----------------
    public List<FollowResponse> getFollowers(Long userId, Long me) {

        return followRepo.findByFolloweeId(userId)
                .stream()
                .map(f -> {
                    User u = f.getFollower();
                    boolean isFollowedByMe =
                            followRepo.findByFollowerIdAndFolloweeId(me, u.getId()).isPresent();

                    return new FollowResponse(
                            u.getId(),
                            u.getUsername(),
                            u.getFirstName(),
                            u.getLastName(),
                            u.getProfilePic(),
                            isFollowedByMe
                    );
                }).toList();
    }

    // ---------------- FOLLOWING LIST ----------------
    public List<FollowResponse> getFollowing(Long userId, Long me) {

        return followRepo.findByFollowerId(userId)
                .stream()
                .map(f -> {
                    User u = f.getFollowee();
                    boolean isFollowedByMe =
                            followRepo.findByFollowerIdAndFolloweeId(me, u.getId()).isPresent();

                    return new FollowResponse(
                            u.getId(),
                            u.getUsername(),
                            u.getFirstName(),
                            u.getLastName(),
                            u.getProfilePic(),
                            isFollowedByMe
                    );
                }).toList();
    }

    // ---------------- COUNT API ----------------
    public Map<String, Integer> getCounts(Long userId) {
        int followers = followRepo.countByFolloweeId(userId);
        int following = followRepo.countByFollowerId(userId);

        return Map.of(
                "followers", followers,
                "following", following
        );
    }
}
