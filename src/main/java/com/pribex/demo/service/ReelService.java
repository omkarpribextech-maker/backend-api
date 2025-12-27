package com.pribex.demo.service;

import com.pribex.demo.model.Reel;
import com.pribex.demo.model.ReelLike;
import com.pribex.demo.repository.ReelLikeRepository;
import com.pribex.demo.repository.ReelRepository;
import com.pribex.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class ReelService {

    @Autowired
    FileStorageService fileStorage;
    @Autowired
    ReelRepository reelRepo;
    @Autowired
    ReelLikeRepository likeRepo;
    @Autowired
    UserRepository userRepo;

    public Reel uploadReel(Long userId, MultipartFile video, String caption) {

        String videoUrl = fileStorage.saveFile(video);

        Reel r = new Reel();
        r.setUser(userRepo.findById(userId).orElseThrow());
        r.setVideoUrl(videoUrl);
        r.setCaption(caption);

        return reelRepo.save(r);
    }

    public Map<String, Object> likeReel(Long reelId, Long userId) {
        Reel reel = reelRepo.findById(reelId).orElseThrow();

        if (!likeRepo.existsByReelIdAndUserId(reelId, userId)) {
            ReelLike rl = new ReelLike();
            rl.setReel(reel);
            rl.setUser(userRepo.findById(userId).orElseThrow());
            likeRepo.save(rl);

            reel.setLikesCount(reel.getLikesCount() + 1);
            reelRepo.save(reel);
        }

        return Map.of(
                "likes", likeRepo.countByReelId(reelId),
                "liked", true
        );
    }

    public List<Reel> listReels() {
        return reelRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Map<String, Object> viewReel(Long reelId) {
        Reel r = reelRepo.findById(reelId).orElseThrow();
        r.setViewsCount(r.getViewsCount() + 1);
        reelRepo.save(r);

        return Map.of("views", r.getViewsCount());
    }
}

