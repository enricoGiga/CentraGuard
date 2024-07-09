package com.security.gateway.repository;


import com.security.gateway.entity.GrantedAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface GrantedAuthorityRepository extends JpaRepository<GrantedAuthority, Integer> {


    @Query(value = "select a.authority from granted_authority a\n" +
            "join user_role ur on ur.user_role_id = a.user_role_id\n" +
            "join application_user_user_role auur on ur.user_role_id = auur.user_role_id\n" +
            "join application_user au on au.email = auur.email\n" +
            "where au.email = :email", nativeQuery = true)
    Set<String> findAllGrantedAuthoritiesWithEmail(
            @Param("email") String email);

}
