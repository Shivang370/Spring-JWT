package org.example.repository;

import org.example.model.OtpDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface otpDao extends JpaRepository<OtpDetails,Integer> {


    @Query("select o from OtpDetails o where o.email =:email")
    OtpDetails findByEmail(@Param(value = "email") String email);
}
