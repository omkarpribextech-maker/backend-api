package com.om.demo.service;

import com.om.demo.dto.CreatePostRequest;
import com.om.demo.model.*;
import com.om.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {

    @Autowired PostRepository postRepo;
    @Autowired PostMediaRepository mediaRepo;
    @Autowired PostLikeRepository likeRepo;
    @Autowired PostCommentRepository commentRepo;
    @Autowired BookmarkRepository bookmarkRepo;
    @Autowired PostShareRepository shareRepo;
    @Autowired NotificationRepository notificationRepo;
    @Autowired UserRepository userRepo;

    public Map<String,Object> createPost(CreatePostRequest req) {
        User user = userRepo.findById(req.userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post p = new Post();
        p.setUser(user);
        p.setCaption(req.caption);
        p.setLocationLat(req.lat);
        p.setLocationLng(req.lng);
        p.setPincode(req.pincode);
        // auto detect type
        if (req.mediaUrls != null && !req.mediaUrls.isEmpty()) {
            String url = req.mediaUrls.get(0).toLowerCase();
            p.setType(url.endsWith(".mp4") ? "VIDEO" : "IMAGE");
        }
        p = postRepo.save(p);

        if (req.mediaUrls != null) {
            int idx = 0;
            for (String url : req.mediaUrls) {
                PostMedia m = new PostMedia();
                m.setPost(p);
                m.setUrl(url);
                m.setOrderIndex(idx++);
                mediaRepo.save(m);
            }
        }
        return Map.of(
                "id", p.getId(),
                "caption", p.getCaption(),
                "type", p.getType(),
                "media", req.mediaUrls,
                "createdAt", p.getCreatedAt()
        );
    }

    public Map<String, Object> getFeed(Pageable pageable, Long userId) {

        Page<Post> page = postRepo.findAllByOrderByCreatedAtDesc(pageable);

        List<Map<String, Object>> posts = page.getContent().stream().map(p -> {
            Map<String, Object> map = new HashMap<>();

            map.put("id", p.getId());
            map.put("caption", p.getCaption());
            map.put("type", p.getType());
            map.put("likesCount", Optional.ofNullable(p.getLikesCount()).orElse(0));
            map.put("commentsCount", Optional.ofNullable(p.getCommentsCount()).orElse(0));
            map.put("sharesCount", Optional.ofNullable(p.getSharesCount()).orElse(0));
            map.put("media", p.getMedia() != null ?
                    p.getMedia().stream().map(m -> m.getUrl()).toList() :
                    Collections.emptyList());
            map.put("likedByMe", likeRepo.findByPostIdAndUserId(p.getId(), userId).isPresent());
            map.put("bookmarkedByMe", bookmarkRepo.findByPostIdAndUserId(p.getId(), userId).isPresent());
            map.put("createdAt", p.getCreatedAt());

            return map;
        }).toList();


        return Map.of(
                "success", true,
                "page", page.getNumber(),
                "totalPages", page.getTotalPages(),
                "posts", posts
        );
    }


    public Map<String,Object> toggleLike(Long postId, Long userId) {

        Post post = postRepo.findById(postId).orElseThrow();

        User user = userRepo.findById(userId).orElseThrow();

        Optional<PostLike> ex = likeRepo.findByPostIdAndUserId(postId, userId);

        boolean liked;

        if (ex.isPresent()) {
            likeRepo.delete(ex.get());
            post.setLikesCount(Math.max(0, post.getLikesCount()-1));
            liked = false;
        } else {
            PostLike pl = new PostLike();
            pl.setPost(post);
            pl.setUser(user);
            likeRepo.save(pl);

            post.setLikesCount(post.getLikesCount()+1);
            liked = true;

            if (!post.getUser().getId().equals(userId)) {
                Notification n = new Notification();
                n.setUser(post.getUser());
                n.setType("LIKE");
                n.setActor(user);
                n.setPostId(postId);
                notificationRepo.save(n);
            }
        }

        postRepo.save(post);

        return Map.of(
                "success", true,
                "liked", liked,
                "likesCount", post.getLikesCount()
        );
    }


    public Map<String, Object> addComment(Long postId, Long userId, String text) {

        Post post = postRepo.findById(postId).orElseThrow();
        User user = userRepo.findById(userId).orElseThrow();

        PostComment c = new PostComment();
        c.setPost(post);
        c.setUser(user);
        c.setText(text);
        c = commentRepo.save(c);

        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepo.save(post);

        if (!post.getUser().getId().equals(userId)) {
            Notification n = new Notification();
            n.setUser(post.getUser());
            n.setActor(user);
            n.setType("COMMENT");
            n.setPostId(postId);
            notificationRepo.save(n);
        }

        return Map.of(
                "success", true,
                "commentId", c.getId(),
                "comment", c.getText(),
                "commentsCount", post.getCommentsCount()
        );
    }


    public Map<String,Object> toggleBookmark(Long postId, Long userId) {

        Optional<Bookmark> ex = bookmarkRepo.findByPostIdAndUserId(postId, userId);

        if (ex.isPresent()) {
            bookmarkRepo.delete(ex.get());
            return Map.of("success", true, "bookmarked", false);
        }

        Bookmark b = new Bookmark();
        b.setPost(postRepo.findById(postId).orElseThrow());
        b.setUser(userRepo.findById(userId).orElseThrow());
        bookmarkRepo.save(b);

        return Map.of("success", true, "bookmarked", true);
    }


    public Map<String, Object> sharePost(Long postId, Long userId, String sharedTo, Long sharedToId) {

        PostShare s = new PostShare();
        s.setPost(postRepo.findById(postId).orElseThrow());
        s.setUser(userRepo.findById(userId).orElseThrow());
        s.setSharedTo(sharedTo);
        s.setSharedToId(sharedToId);

        shareRepo.save(s);

        Post p = postRepo.findById(postId).orElseThrow();
        p.setSharesCount(p.getSharesCount() + 1);
        postRepo.save(p);

        return Map.of(
                "success", true,
                "sharesCount", p.getSharesCount()
        );
    }

}
