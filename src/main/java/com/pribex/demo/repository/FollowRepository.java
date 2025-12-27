package com.pribex.demo.repository;

import com.pribex.demo.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    int countByFolloweeId(Long userId);
    int countByFollowerId(Long userId);

    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    List<Follow> findByFolloweeId(Long userId);   // followers
    List<Follow> findByFollowerId(Long userId);   // following
}
