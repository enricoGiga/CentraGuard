package com.security.gateway.controller;

import com.security.gateway.dto.ApplicationUserDTO;
import com.security.gateway.jwt.JwtUtils;
import com.security.gateway.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;


@RestController
@Log4j2
@RequestMapping("api/")
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;

        this.jwtUtils = jwtUtils;
    }

    //todo: CHANGE ROLES
    @GetMapping(path = "{email}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Optional<ApplicationUserDTO> getUserWithId(@PathVariable String email) {
        return userService.findUserByEmail(email);
    }

    @GetMapping
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<ApplicationUserDTO> getUsers() {
        return userService.findAll();
    }


    @PostMapping("createUser")
    public ResponseEntity<String> createNewUser(@RequestBody ApplicationUserDTO applicationUser,
                                                HttpServletRequest request) {

        this.userService.insertNewApplicationUserName(applicationUser, request);

        return ResponseEntity.status(HttpStatus.CREATED).body("User saved");
    }



    @RequestMapping(value = "refreshToken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Claims claims = (Claims) request.getAttribute("claims");

        String newToken = jwtUtils.generateRefreshToken(claims);
        jwtUtils.addTokenToAuthorizationHeaderResponse(newToken, response);
        return ResponseEntity.ok().build();
    }


}