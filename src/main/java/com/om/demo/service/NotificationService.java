package com.om.demo.service;

import com.om.demo.model.Notification;
import com.om.demo.repository.NotificationRepository;
import com.om.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationService {

    @Autowired private NotificationRepository notificationRepo;
    @Autowired private UserRepository userRepo;

    // ----------------------------- CREATE -----------------------------------

    public void send(Long recipientId, Long actorId, String type,
                     Long postId, Long storyId, String payload) {

        Notification n = new Notification();
        n.setUser(userRepo.findById(recipientId).orElseThrow());
        n.setActor(userRepo.findById(actorId).orElseThrow());
        n.setType(type);
        n.setPostId(postId);
        n.setStoryId(storyId);
        n.setPayload(payload);
        n.setCreatedAt(Instant.now());
        n.setRead(false);

        notificationRepo.save(n);
    }

    // ----------------------------- GET -----------------------------------

    public List<Notification> getAll(Long userId) {
        return notificationRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnread(Long userId) {
        return notificationRepo.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getToday(Long userId) {
        Instant start = Instant.now().truncatedTo(ChronoUnit.DAYS);
        return notificationRepo.findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(userId, start);
    }

    public List<Notification> getYesterday(Long userId) {
        Instant start = Instant.now().truncatedTo(ChronoUnit.DAYS).minus(1, ChronoUnit.DAYS);
        Instant end = start.plus(1, ChronoUnit.DAYS);
        return notificationRepo.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start, end);
    }

    // ----------------------------- MARK READ -----------------------------------

    public void markRead(Long notificationId) {
        Notification n = notificationRepo.findById(notificationId).orElseThrow();
        n.setRead(true);
        notificationRepo.save(n);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepo.markAllAsRead(userId);
    }

}
