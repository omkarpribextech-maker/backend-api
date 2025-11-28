package com.om.demo.repository;

import com.om.demo.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(Long userId, Instant after);

    List<Notification> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long userId, Instant from, Instant to);
}
