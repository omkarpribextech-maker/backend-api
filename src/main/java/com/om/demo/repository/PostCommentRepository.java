package com.om.demo.repository;

import com.om.demo.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostIdOrderByCreatedAtDesc(Long postId);
    int countByPostId(Long postId);
}
