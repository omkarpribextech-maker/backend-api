package com.om.demo.repository;

import com.om.demo.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    int countByFolloweeId(Long userId);
    int countByFollowerId(Long userId);
    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
