package com.pribex.demo.repository;


import com.pribex.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    User findByEmail(String email);
    User findByPhone(String phone);

    @Query("""
           SELECT u FROM User u
           WHERE 
               LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%')) OR
               LOWER(u.firstName) LIKE LOWER(CONCAT('%', :q, '%')) OR
               LOWER(u.lastName) LIKE LOWER(CONCAT('%', :q, '%'))
           """)
    List<User> searchUsers(String q);
    @Query("""
       SELECT COUNT(f) > 0 FROM Friend f
       WHERE (f.user1.id = :u1 AND f.user2.id = :u2)
          OR (f.user1.id = :u2 AND f.user2.id = :u1)
       """)
    boolean areFriends(Long u1, Long u2);


}