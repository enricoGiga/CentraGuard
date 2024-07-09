package com.security.gateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Configuration
public class JwtUtils {
    private final JwtConfig jwtConfig;
    private final JwtSecretKey secretKey;

    public JwtUtils(JwtConfig jwtConfig, JwtSecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    public String generateRefreshToken(Claims claims) {
        String sub = claims.get("sub").toString();
        return Jwts.builder()
                .setSubject(sub)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getTokenExpirationDateInMs()))
                .signWith(secretKey.secretKey())
                .compact();

    }

    public void addTokenToAuthorizationHeaderResponse(String token, HttpServletResponse response) {
        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);

    }

    public Jws<Claims> readingJWS(HttpServletRequest request) {
        Jws<Claims> jws = null;

        try {
            String jwsString = request.getHeader(jwtConfig.getAuthorizationHeader());

            jws = Jwts.parserBuilder()  // (1)
                    .setSigningKey(secretKey.secretKey())         // (2)
                    .build()                    // (3)
                    .parseClaimsJws(jwsString.substring(6)); // (4)
            // we can safely trust the JWT
        } catch (JwtException ex) {       // (5)

            // we *cannot* use the JWT as intended by its creator
        }
        return jws;
    }

}
