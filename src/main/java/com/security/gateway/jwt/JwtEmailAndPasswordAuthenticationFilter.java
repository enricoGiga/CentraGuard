package com.security.gateway.jwt;

import com.security.gateway.auth.ApplicationUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.gateway.entity.ApplicationUser;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtEmailAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final JwtSecretKey secretKey;
    private final JwtUtils jwtUtils;
    private final HandlerExceptionResolver resolver;
    private static final String ERROR_MESSAGE = "Something went wrong while parsing /login request body";

    public JwtEmailAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig, JwtSecretKey secretKey, JwtUtils jwtUtils, HandlerExceptionResolver resolver) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.jwtUtils = jwtUtils;
        this.resolver = resolver;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            EmailAndPasswordAuthenticationRequest authenticationRequest =
                    new ObjectMapper().readValue(request.getInputStream(), EmailAndPasswordAuthenticationRequest.class);

            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword());
            setDetails(request, token);

            return authenticationManager.authenticate(token);

        } catch (IOException e) {
            throw new InternalAuthenticationServiceException(ERROR_MESSAGE, e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        ApplicationUserDetails principal = (ApplicationUserDetails) authResult.getPrincipal();
        ApplicationUser applicationUser = principal.getApplicationUser();
        String token = Jwts.builder()
                .setSubject(applicationUser.getEmail())
                .claim("authorities", authResult.getAuthorities())
                .claim("firstName", applicationUser.getFirstName())
                .claim("lastName", applicationUser.getLastName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getTokenExpirationDateInMs()))
                .signWith(secretKey.secretKey())
                .compact();
        jwtUtils.addTokenToAuthorizationHeaderResponse(token, response);
        //response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);


    }

}
