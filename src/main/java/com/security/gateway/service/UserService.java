package com.security.gateway.service;

import com.security.gateway.dto.ApplicationUserDTO;
import com.security.gateway.entity.VerificationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface UserService {
    ApplicationUserDTO save(ApplicationUserDTO applicationUserDTO);

    Optional<ApplicationUserDTO> findUserByEmail(String email);

    List<ApplicationUserDTO> findAll();

    void insertNewApplicationUserName(ApplicationUserDTO person, HttpServletRequest request);
    void createVerificationToken(ApplicationUserDTO user, String token);
    VerificationToken findVerificationToken(String token);
    String confirmRegistration(String token);

}
