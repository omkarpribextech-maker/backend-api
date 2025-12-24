package com.om.demo.service;

import com.om.demo.dto.CreateStoryRequest;
import com.om.demo.dto.StoryResponse;
import com.om.demo.model.*;
import com.om.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class StoryService {

    @Autowired NotificationService notificationService;

    @Autowired StoryRepository storyRepo;
    @Autowired StoryViewRepository viewRepo;
    @Autowired NotificationRepository notificationRepo;
    @Autowired UserRepository userRepo;
    public StoryResponse createStory(CreateStoryRequest req) {

        Story story = new Story();

        story.setUser(userRepo.findById(req.getUserId()).orElseThrow());

        story.setMediaUrl(req.getMediaUrl());
        story.setType(req.getMediaType());
        story.setContentType(req.getContentType());

        story.setWidth(req.getWidth());
        story.setHeight(req.getHeight());
        story.setAspectRatio(req.getAspectRatio());
        story.setDuration(req.getDuration());

        story.setLocationLat(req.getLat());
        story.setLocationLng(req.getLng());

        story.setPrivacy(req.getPrivacy());

        story.setHideUserIds(req.getHideUserIds());
        story.setOnlyUserIds(req.getOnlyUserIds());
        story.setStoryId(req.getStoryId());
        story.setChunkIndex(req.getChunkIndex());
        story.setParentVideoId(req.getParentVideoId());
        story.setIsLastChunk(req.getIsLastChunk());
        story.setThumbnailUrl(req.getThumbnailUrl());
        story.setStatus(req.getStatus());


        story.setExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));

        Story saved = storyRepo.save(story);

        return mapToResponse(saved);
    }


    static class VideoInfo {
        int width;
        int height;
        double duration;
        int fps;
    }

    private VideoInfo extractVideoInfo(MultipartFile file) throws Exception {
        File temp = File.createTempFile("video", ".mp4");
        file.transferTo(temp);

        VideoInfo info = new VideoInfo();

        Process p = new ProcessBuilder(
                "ffprobe", "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=width,height,duration,r_frame_rate",
                "-of", "default=noprint_wrappers=1",
                temp.getAbsolutePath()
        ).start();

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;

        while ((line = br.readLine()) != null) {

            if (line.startsWith("width="))
                info.width = Integer.parseInt(line.split("=")[1]);

            if (line.startsWith("height="))
                info.height = Integer.parseInt(line.split("=")[1]);

            if (line.startsWith("duration="))
                info.duration = Double.parseDouble(line.split("=")[1]);

            if (line.startsWith("r_frame_rate=")) {
                String[] f = line.split("=")[1].split("/");
                info.fps = Integer.parseInt(f[0]) / Integer.parseInt(f[1]);
            }
        }

        temp.delete();
        return info;
    }

    public StoryResponse mapToResponse(Story s) {
        StoryResponse r = new StoryResponse();

        r.setId(s.getId());
        r.setMediaUrl(s.getMediaUrl());
        r.setMediaType(s.getType());
        r.setContentType(s.getContentType());

        r.setWidth(s.getWidth());
        r.setHeight(s.getHeight());
        r.setAspectRatio(s.getAspectRatio());
        r.setDuration(s.getDuration());

        r.setLocationLat(s.getLocationLat());
        r.setLocationLng(s.getLocationLng());

        r.setCreatedAt(s.getCreatedAt());
        r.setExpiresAt(s.getExpiresAt());
        r.setViewsCount(s.getViewsCount());

        r.setUser(s.getUser());

        r.setStoryId(s.getStoryId());
        r.setChunkIndex(s.getChunkIndex());
        r.setParentVideoId(s.getParentVideoId());
        r.setIsLastChunk(s.getIsLastChunk());
        r.setThumbnailUrl(s.getThumbnailUrl());
        r.setStatus(s.getStatus());


        return r;
    }

    @Transactional
    public Map<String, Object> viewStory(Long storyId, Long viewerId) {

        Story story = storyRepo.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found"));

        User viewer = userRepo.findById(viewerId)
                .orElseThrow(() -> new RuntimeException("Viewer not found"));

        StoryView existingView = viewRepo.findByStoryIdAndViewerId(storyId, viewerId)
                .orElse(null);

        if (existingView == null) {
            StoryView view = new StoryView();
            view.setStory(story);
            view.setViewer(viewer);
            viewRepo.save(view);

            // SEND NOTIFICATION
            if (!story.getUser().getId().equals(viewerId)) {
                notificationService.send(
                        story.getUser().getId(),   // story owner
                        viewerId,                  // actor
                        "STORY_VIEW",
                        null,
                        storyId,
                        null
                );
            }
        }

        int views = viewRepo.countByStoryId(storyId);

        return Map.of(
                "storyId", storyId,
                "viewerId", viewerId,
                "views", views,
                "success", true
        );
    }



    public List<Story> getActiveStories() {
        return storyRepo.findByExpiresAtAfterOrderByCreatedAtDesc(Instant.now());
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void deleteExpired() {
        Instant now = Instant.now();
        List<Story> expired = storyRepo.findByExpiresAtBefore(now);

        for (Story story : expired) {
            viewRepo.deleteAllByStoryId(story.getId());
            storyRepo.delete(story);
        }

        System.out.println("Expired stories cleaned: " + expired.size());
    }


    public boolean canUserSeeStory(Story story, Long viewerId) {

        Long ownerId = story.getUser().getId();

        // OWNER can always see
        if (viewerId.equals(ownerId)) return true;

        switch (story.getPrivacy()) {

            case "PUBLIC":
                return true;

            case "FRIENDS":
                // TODO: friend check logic
                return userRepo.areFriends(ownerId, viewerId);

            case "HIDE":
                if (story.getHideUserIds() != null &&
                        story.getHideUserIds().contains(viewerId)) {
                    return false;
                }
                return true;

            case "ONLY":
                return story.getOnlyUserIds() != null &&
                        story.getOnlyUserIds().contains(viewerId);

            default:
                return true;
        }
    }

    public List<StoryResponse> getActiveStories(Long viewerId) {

        List<Story> all = storyRepo.findByExpiresAtAfterOrderByCreatedAtDesc(Instant.now());

        return all.stream()
                .filter(story -> canUserSeeStory(story, viewerId))
                .map(this::mapToResponse)
                .toList();
    }


}
