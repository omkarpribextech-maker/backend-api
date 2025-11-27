package com.om.demo.repository;

import com.om.demo.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    Otp findTopByIdentifierAndPurposeOrderByIdDesc(String identifier, String purpose);
}
