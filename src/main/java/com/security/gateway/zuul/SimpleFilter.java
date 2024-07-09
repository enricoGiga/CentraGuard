package com.security.gateway.zuul;

import com.security.gateway.jwt.JwtConfig;
import com.security.gateway.jwt.JwtSecretKey;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class SimpleFilter extends ZuulFilter {

    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private JwtSecretKey secretKey;

    private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String requestURI = request.getRequestURI();

        if (requestURI.contains("/ocr_api/")) {
            try {
                String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

                if (authorizationHeader == null || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
                    log.warn("Missing or invalid Authorization header");
                    return null;
                }

                String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "").trim();

                if (token.isEmpty()) {
                    log.warn("Token is empty");
                    return null;
                }

                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(secretKey.secretKey())
                        .build()
                        .parseClaimsJws(token);

                Claims body = claimsJws.getBody();
                String userName = body.getSubject();

                // The header name should preferably be lowercase, and Zuul filter will convert it to lowercase
                ctx.addZuulRequestHeader("cur_user_id", userName);

                log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

            } catch (JwtException e) {
                log.error("JWT parsing failed", e);
                throw new ZuulException(e, 401, "Unauthorized");
            } catch (Exception e) {
                log.error("Error occurred in Zuul filter", e);
                throw new ZuulException(e, 500, "Internal Server Error");
            }
        }

        return null; // This is expected behavior for a pre-filter
    }


}
