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

    public Post createPost(CreatePostRequest req) {
        User user = userRepo.findById(req.userId).orElseThrow();
        Post p = new Post();
        p.setUser(user);
        p.setCaption(req.caption);
        p.setLocationLat(req.lat);
        p.setLocationLng(req.lng);
        p.setPincode(req.pincode);
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
        return p;
    }

    public Page<Post> getFeed(Pageable pageable) {
        return postRepo.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Map<String,Object> toggleLike(Long postId, Long userId) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found: " + postId));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

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

        return Map.of("liked", liked, "likesCount", post.getLikesCount());
    }


    public PostComment addComment(Long postId, Long userId, String text) {
        Post post = postRepo.findById(postId).orElseThrow();
        PostComment c = new PostComment();
        c.setPost(post);
        c.setUser(userRepo.findById(userId).orElseThrow());
        c.setText(text);
        c = commentRepo.save(c);
        post.setCommentsCount(post.getCommentsCount()+1);
        postRepo.save(post);

        if (!post.getUser().getId().equals(userId)) {
            Notification n = new Notification();
            n.setUser(post.getUser());
            n.setType("COMMENT");
            n.setActor(userRepo.findById(userId).orElseThrow());
            n.setPostId(postId);
            notificationRepo.save(n);
        }
        return c;
    }

    public String toggleBookmark(Long postId, Long userId) {
        Optional<Bookmark> ex = bookmarkRepo.findByPostIdAndUserId(postId, userId);
        if (ex.isPresent()) {
            bookmarkRepo.delete(ex.get());
            return "Unbookmarked";
        } else {
            Bookmark b = new Bookmark();
            b.setPost(postRepo.findById(postId).orElseThrow());
            b.setUser(userRepo.findById(userId).orElseThrow());
            bookmarkRepo.save(b);
            return "Bookmarked";
        }
    }

    public String sharePost(Long postId, Long userId, String sharedTo, Long sharedToId) {
        PostShare s = new PostShare();
        s.setPost(postRepo.findById(postId).orElseThrow());
        s.setUser(userRepo.findById(userId).orElseThrow());
        s.setSharedTo(sharedTo);
        s.setSharedToId(sharedToId);
        shareRepo.save(s);
        Post p = postRepo.findById(postId).orElseThrow();
        p.setSharesCount(p.getSharesCount()+1);
        postRepo.save(p);
        return "Shared";
    }
}
