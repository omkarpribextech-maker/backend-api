package com.om.demo.service;

import com.om.demo.model.Notification;
import com.om.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationService {

    @Autowired NotificationRepository notificationRepo;

    public List<Notification> getToday(Long userId) {
        Instant start = Instant.now().truncatedTo(ChronoUnit.DAYS);
        return notificationRepo.findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(userId, start);
    }

    public List<Notification> getYesterday(Long userId) {
        Instant start = Instant.now().truncatedTo(ChronoUnit.DAYS).minus(1, ChronoUnit.DAYS);
        Instant end = start.plus(1, ChronoUnit.DAYS);
        return notificationRepo.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start, end);
    }
}
