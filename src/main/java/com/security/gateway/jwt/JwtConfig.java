package com.security.gateway.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationDateInMs;

    public JwtConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Integer getTokenExpirationDateInMs() {
        return tokenExpirationDateInMs;
    }

    public void setTokenExpirationDateInMs(Integer tokenExpirationDateInMs) {
        this.tokenExpirationDateInMs = tokenExpirationDateInMs;
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;

    }

}

