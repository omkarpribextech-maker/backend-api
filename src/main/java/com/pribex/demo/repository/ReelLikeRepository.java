package com.pribex.demo.repository;

import com.pribex.demo.model.ReelLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReelLikeRepository extends JpaRepository<ReelLike, Long> {
    boolean existsByReelIdAndUserId(Long reelId, Long userId);
    int countByReelId(Long reelId);
}
