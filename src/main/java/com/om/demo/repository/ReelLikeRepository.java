package com.om.demo.repository;

import com.om.demo.model.ReelLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReelLikeRepository extends JpaRepository<ReelLike, Long> {
    boolean existsByReelIdAndUserId(Long reelId, Long userId);
    int countByReelId(Long reelId);
}
