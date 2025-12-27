package com.pribex.demo.repository;


import com.pribex.demo.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {}