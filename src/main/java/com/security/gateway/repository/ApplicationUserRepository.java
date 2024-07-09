package com.security.gateway.repository;


import com.security.gateway.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, String> {
    @Modifying
    @Query(value = "update application_user set  password = :password where email = :user_email", nativeQuery = true)
    void changePassword(@Param("user_email") String userEmail, @Param("password") String password);


    @Modifying
    @Query(value = "update application_user set is_enabled = true where email = :user_email", nativeQuery = true)
    void updateUserToEnabled(@Param("user_email") String userEmail);
}
