package com.pribex.demo.service;

import com.pribex.demo.dto.CreateStoryRequest;
import com.pribex.demo.dto.StoryItemResponse;
import com.pribex.demo.dto.StoryResponse;

import com.pribex.demo.enums.PrivacyType;
import com.pribex.demo.enums.StoryItemType;
import com.pribex.demo.enums.StoryType;
import com.pribex.demo.model.Story;
import com.pribex.demo.model.StoryItem;
import com.pribex.demo.model.StoryView;
import com.pribex.demo.model.User;
import com.pribex.demo.repository.NotificationRepository;
import com.pribex.demo.repository.StoryRepository;
import com.pribex.demo.repository.StoryViewRepository;
import com.pribex.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    StoryRepository storyRepo;
    @Autowired
    StoryViewRepository viewRepo;
    @Autowired
    NotificationRepository notificationRepo;
    @Autowired
    UserRepository userRepo;
    @Transactional
    public StoryResponse createStory(CreateStoryRequest req, Long authUserId) {

        User user = userRepo.findById(authUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Story story = new Story();
        story.setStoryId(req.getStoryId());
        story.setUser(user);
        story.setStoryType(req.getStoryType());

        story.setPrivacy(req.getPrivacy().getType());

        story.setAllowedUserIds(req.getPrivacy().getAllowedUserIds());
        story.setBlockedUserIds(req.getPrivacy().getBlockedUserIds());

        story.setCreatedAt(Instant.now());
        story.setExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));

        List<StoryItem> items = req.getItems().stream().map(i -> {

            if (req.getStoryType() == StoryType.IMAGE_STORY &&
                    i.getItemType() != StoryItemType.IMAGE)
                throw new RuntimeException("IMAGE_STORY cannot contain video");

            if (req.getStoryType() == StoryType.VIDEO_STORY &&
                    i.getItemType() != StoryItemType.VIDEO_CHUNK)
                throw new RuntimeException("VIDEO_STORY cannot contain image");

            StoryItem item = new StoryItem();
            item.setStory(story);
            item.setItemId(i.getItemId());
            item.setItemType(i.getItemType());
            item.setMediaUrl(i.getMediaUrl());
            item.setDurationSeconds(i.getDurationSeconds());
            item.setOrderIndex(i.getOrderIndex());

            if (i.getChunkInfo() != null) {
                item.setOriginalVideoId(i.getChunkInfo().getOriginalVideoId());
                item.setChunkIndex(i.getChunkInfo().getChunkIndex());
                item.setTotalChunks(i.getChunkInfo().getTotalChunks());
                item.setStartMs(i.getChunkInfo().getStartMs());
                item.setEndMs(i.getChunkInfo().getEndMs());
            }

            item.setEdits(i.getEdits());
            item.setMusic(i.getMusic());
            item.setTextOverlays(i.getTextOverlays());

            return item;

        }).toList();

        story.setItems(items);

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

        // BASIC
        r.setId(s.getId());
        r.setStoryId(s.getStoryId());
        r.setContentType("story");
        r.setCreatedAt(s.getCreatedAt());
        r.setExpiresAt(s.getExpiresAt());
        r.setViewsCount(s.getViewsCount());
        r.setUser(s.getUser());
        r.setStatus(s.getStatus());
        r.setThumbnailUrl(s.getThumbnailUrl());

        // STORY TYPE SPECIFIC
        if (s.getStoryType() == StoryType.VIDEO_STORY) {
            r.setMediaUrl(s.getMediaUrl());
            r.setMediaType("VIDEO");
            r.setWidth(s.getWidth());
            r.setHeight(s.getHeight());
            r.setAspectRatio(s.getAspectRatio());
            r.setDuration(s.getDuration());
            r.setChunkIndex(s.getChunkIndex());
            r.setParentVideoId(s.getParentVideoId());
            r.setIsLastChunk(s.getIsLastChunk());
        }

        //  ITEMS (MOST IMPORTANT FOR IMAGE_STORY)
        List<StoryItemResponse> itemResponses =
                s.getItems().stream().map(item -> {

                    StoryItemResponse ir = new StoryItemResponse();
                    ir.setItemId(item.getItemId());
                    ir.setItemType(item.getItemType());
                    ir.setMediaUrl(item.getMediaUrl());
                    ir.setDurationSeconds(item.getDurationSeconds());
                    ir.setOrderIndex(item.getOrderIndex());
                    ir.setEdits(item.getEdits());
                    ir.setMusic(item.getMusic());
                    ir.setTextOverlays(item.getTextOverlays());


                    return ir;
                }).toList();

        r.setItems(itemResponses);

        return r;
    }


    @Transactional
    public Map<String, Object> viewStory(Long storyId, Long viewerId) {

        Story story = storyRepo.findById(storyId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found"));

        User viewer = userRepo.findById(viewerId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Viewer not found"));

        StoryView existingView =
                viewRepo.findByStory_IdAndViewer_Id(storyId, viewerId).orElse(null);

        if (existingView == null) {
            StoryView view = new StoryView();
            view.setStory(story);
            view.setViewer(viewer);
            viewRepo.save(view);

            if (story.getUser() != null &&
                    !story.getUser().getId().equals(viewerId)) {

                notificationService.send(
                        story.getUser().getId(),
                        viewerId,
                        "STORY_VIEW",
                        null,
                        storyId,
                        null
                );
            }
        }

        int views = viewRepo.countByStory_Id(storyId);

        return Map.of(
                "storyId", storyId,
                "viewerId", viewerId,
                "views", views,
                "success", true
        );
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

        if (story.getUser() == null || story.getUser().getId() == null) {
            return false; // safety
        }

        Long ownerId = story.getUser().getId();

        if (viewerId.equals(ownerId)) return true;

        PrivacyType privacy = story.getPrivacy();

        if (privacy == null) return true;

        switch (privacy) {

            case PUBLIC:
                return true;

            case FRIENDS:
            case ALL_FRIENDS:
                return userRepo.areFriends(ownerId, viewerId);

            case ONLY:
                return story.getOnlyUserIds() != null &&
                        story.getOnlyUserIds().contains(viewerId);

            case HIDE:
                return story.getHideUserIds() == null ||
                        !story.getHideUserIds().contains(viewerId);

            default:
                return true;
        }
    }



    public List<StoryResponse> getActiveStories(Long viewerId) {

        List<Story> all =
                storyRepo.findByExpiresAtAfterOrderByCreatedAtDesc(Instant.now());

        return all.stream()
                .filter(story -> canUserSeeStory(story, viewerId))
                .map(this::mapToResponse)
                .toList();
    }



}
