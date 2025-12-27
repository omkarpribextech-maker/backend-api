package com.pribex.demo.repository;

import com.pribex.demo.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE LOWER(p.caption) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Post> searchPosts(String q);

    @Query("SELECT p FROM Post p ORDER BY (p.likesCount + p.commentsCount + p.sharesCount) DESC")
    List<Post> findTrending(Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.id IN :interestIds")
    List<Post> findByInterests(List<Long> interestIds);

}
