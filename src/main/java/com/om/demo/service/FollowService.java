package com.om.demo.service;

import com.om.demo.model.Follow;
import com.om.demo.model.Notification;
import com.om.demo.model.User;
import com.om.demo.repository.FollowRepository;
import com.om.demo.repository.NotificationRepository;
import com.om.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FollowService {

    @Autowired NotificationService notificationService;

    @Autowired FollowRepository followRepo;
    @Autowired UserRepository userRepo;
    @Autowired NotificationRepository notificationRepo;

    public String follow(Long followerId, Long targetUserId) {
        Optional<Follow> exists = followRepo.findByFollowerIdAndFolloweeId(followerId, targetUserId);
        if (exists.isPresent()) return "Already following";

        Follow f = new Follow();
        f.setFollower(userRepo.findById(followerId).orElseThrow());
        f.setFollowee(userRepo.findById(targetUserId).orElseThrow());
        followRepo.save(f);

        notificationService.send(
                targetUserId,
                followerId,
                "FOLLOW",
                null,
                null,
                "{\"message\":\"started following you\"}"
        );

        return "Followed";


    }

    public String unfollow(Long followerId, Long targetUserId) {
        Optional<Follow> ex = followRepo.findByFollowerIdAndFolloweeId(followerId, targetUserId);
        if (ex.isPresent()) {
            followRepo.delete(ex.get());
            return "Unfollowed";
        }
        return "Not following";
    }
}
