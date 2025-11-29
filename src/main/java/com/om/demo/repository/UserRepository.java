package com.om.demo.repository;


import com.om.demo.model.User;
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

}