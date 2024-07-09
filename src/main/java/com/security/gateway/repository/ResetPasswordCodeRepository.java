package com.security.gateway.repository;

import com.security.gateway.entity.ResetPasswordCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResetPasswordCodeRepository extends JpaRepository<ResetPasswordCode, String> {

    @Modifying
    @Query(value = "insert into reset_password_code(user_email, reset_code) values (:user_email,:reset_code)", nativeQuery = true)
    void addResetCode(
            @Param("user_email") String userEmail, @Param("reset_code") Integer resetCode);

    @Modifying
    @Query(value = "update reset_password_code set  verified = true where user_email = :user_email", nativeQuery = true)
    void setCodeAsVerified(@Param("user_email") String userEmail);

}
