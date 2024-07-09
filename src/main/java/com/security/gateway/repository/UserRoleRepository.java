package com.security.gateway.repository;


import com.security.gateway.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    @Modifying
    @Query(value = "insert into application_user_user_role(email, user_role_id) values (:email, :user_role_id)", nativeQuery = true)
    void addRoleToUser(
            @Param("email") String email,@Param("user_role_id") Integer userRoleId);
}
