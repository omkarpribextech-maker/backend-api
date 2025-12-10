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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


        story.setExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));

        Story saved = storyRepo.save(story);

        return mapToResponse(saved);
    }


//    public StoryResponse createStory(Long userId, MultipartFile media, Double lat, Double lng) throws Exception {
//
//        // Limit check
//        Instant last24 = Instant.now().minus(24, ChronoUnit.HOURS);
//        int count = storyRepo.countByUserIdAndCreatedAtAfter(userId, last24);
//        if (count >= 15)
//            throw new RuntimeException("Story limit exceeded (Max 15 stories per 24 hours)");
//
//        // File size validation
//        long sizeMB = media.getSize() / (1024 * 1024);
//        if (sizeMB > 10)
//            throw new RuntimeException("Story file too large (max 10MB)");
//
//        // Media type
//        String contentType = media.getContentType();
//        boolean isVideo = contentType.startsWith("video");
//        boolean isImage = contentType.startsWith("image");
//
//        if (!isVideo && !isImage)
//            throw new RuntimeException("Only image/video allowed");
//
//        int width = 0, height = 0, fps = 0;
//        double duration = 0;
//
//        // Extract info
//        if (isVideo) {
//            VideoInfo info = extractVideoInfo(media);
//            width = info.width;
//            height = info.height;
//            duration = info.duration;
//            fps = info.fps;
//
//            if (duration > 20)
//                throw new RuntimeException("Max 20 seconds video allowed");
//
//            if (height > 720)
//                throw new RuntimeException("Max 720p allowed");
//
//            if (fps > 30)
//                throw new RuntimeException("Max 30fps allowed");
//
//        } else {
//            BufferedImage img = ImageIO.read(media.getInputStream());
//            width = img.getWidth();
//            height = img.getHeight();
//
//            if (width > 1080)
//                throw new RuntimeException("Image width max 1080px allowed");
//        }
//
//        double aspectRatio = (double) width / height;
//
//        // SAVE file
//        String fileName = System.currentTimeMillis() + "_" + media.getOriginalFilename();
//        Path savePath = Paths.get("uploads/stories/" + fileName);
//        Files.createDirectories(savePath.getParent());
//        Files.write(savePath, media.getBytes());
//        String mediaUrl = "/uploads/stories/" + fileName;
//
//        // Save Story entity
//        Story s = new Story();
//        s.setUser(userRepo.findById(userId).orElseThrow());
//
//        s.setMediaUrl(mediaUrl);
//        s.setType(isVideo ? "video" : "image");
//        s.setContentType("story");
//
//        s.setWidth(width);
//        s.setHeight(height);
//        s.setAspectRatio(aspectRatio);
//        s.setDuration(duration);
//        s.setFps(fps);
//
//        s.setLocationLat(lat);
//        s.setLocationLng(lng);
//
//        s.setExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));
//
//        Story saved = storyRepo.save(s);
//
//        // RETURN FINAL RESPONSE
//        return mapToResponse(saved);
//    }

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
