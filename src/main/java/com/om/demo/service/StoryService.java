package com.om.demo.service;

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

    @Autowired StoryRepository storyRepo;
    @Autowired StoryViewRepository viewRepo;
    @Autowired NotificationRepository notificationRepo;
    @Autowired UserRepository userRepo;

    public Story createStory(Long userId, MultipartFile media, Double lat, Double lng) throws Exception {

        // 1. Limit check: Max 15 stories per 24 Hours
        Instant last24 = Instant.now().minus(24, ChronoUnit.HOURS);
        int count = storyRepo.countByUserIdAndCreatedAtAfter(userId, last24);
        if (count >= 15)
            throw new RuntimeException("Story limit exceeded (Max 15 stories per 24 hours)");

        // 2. File-size validation
        long sizeMB = media.getSize() / (1024 * 1024);

        if (sizeMB > 10)
            throw new RuntimeException("Story file too large. Max allowed size = 10MB.");

        if (sizeMB > 8)
            System.out.println("⚠ Warning: File above acceptable limit (>8MB)");

        if (sizeMB > 5)
            System.out.println("⚠ Note: Story file is above ideal size (>5MB). Allowed.");

        // 3. Detect media type
        String contentType = media.getContentType();
        boolean isVideo = contentType != null && contentType.startsWith("video");
        boolean isImage = contentType != null && contentType.startsWith("image");

        if (!isImage && !isVideo)
            throw new RuntimeException("Invalid media type. Only image/video accepted");

        int width = 0, height = 0, fps = 0;
        double duration = 0;

        // 4. Validate media
        if (isVideo) {

            VideoInfo info = extractVideoInfo(media);
            width = info.width;
            height = info.height;
            duration = info.duration;
            fps = info.fps;

            if (duration > 20)
                throw new RuntimeException("Video too long. Max 20 seconds allowed");

            if (height > 720)
                throw new RuntimeException("Video resolution too high. Max 720p allowed");

            if (fps > 30)
                throw new RuntimeException("FPS too high. Max 30 FPS allowed");

        } else {
            BufferedImage img = ImageIO.read(media.getInputStream());
            width = img.getWidth();
            height = img.getHeight();

            if (width > 1080)
                throw new RuntimeException("Image width max 1080px allowed");
        }

        double aspectRatio = (height > 0) ? (double) width / height : 0;

        // 5. Save file
        String fileName = System.currentTimeMillis() + "_" + media.getOriginalFilename();
        Path savePath = Paths.get("uploads/stories/" + fileName);
        Files.createDirectories(savePath.getParent());
        Files.write(savePath, media.getBytes());

        String mediaUrl = "/uploads/stories/" + fileName;

        // 6. Save story entity
        Story s = new Story();
        s.setUser(userRepo.findById(userId).orElseThrow());
        s.setMediaUrl(mediaUrl);
        s.setType(isVideo ? "video" : "image");
        s.setContentType("story");
        s.setExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));

        s.setWidth(width);
        s.setHeight(height);
        s.setAspectRatio(aspectRatio);
        s.setDuration(duration);
        s.setFps(fps);

        s.setLocationLat(lat);
        s.setLocationLng(lng);

        return storyRepo.save(s);
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
                "ffprobe",
                "-v", "error",
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

    public Map<String, Object> viewStory(Long storyId, Long viewerId) {

        Optional<StoryView> ex = viewRepo.findByStoryIdAndViewerId(storyId, viewerId);
        Story story = storyRepo.findById(storyId).orElseThrow();

        if (ex.isEmpty()) {

            StoryView sv = new StoryView();
            sv.setStory(story);
            sv.setViewer(userRepo.findById(viewerId).orElseThrow());
            viewRepo.save(sv);

            story.setViewsCount(story.getViewsCount() + 1);
            storyRepo.save(story);

            // Notification for story owner
            if (!story.getUser().getId().equals(viewerId)) {
                Notification n = new Notification();
                n.setUser(story.getUser());
                n.setType("STORY_VIEW");
                n.setActor(userRepo.findById(viewerId).orElseThrow());
                n.setStoryId(storyId);
                notificationRepo.save(n);
            }
        }

        int viewers = viewRepo.countByStoryId(storyId);
        return Map.of(
                "views", viewers,
                "viewsCount", story.getViewsCount()
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



}
