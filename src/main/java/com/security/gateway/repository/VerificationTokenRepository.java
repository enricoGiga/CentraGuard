package com.security.gateway.repository;

import com.security.gateway.entity.ApplicationUser;
import com.security.gateway.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(ApplicationUser user);
}