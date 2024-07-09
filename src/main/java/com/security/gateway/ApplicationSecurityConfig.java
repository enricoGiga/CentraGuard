package com.security.gateway;


import com.security.gateway.auth.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.gateway.jwt.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtConfig jwtConfig;
    private final JwtSecretKey secretKey;
    private final String DICTIONARY_PROJECT_PATH = "/dictionary/";
    private final HandlerExceptionResolver resolver;
    private final JwtUtils jwtUtils;


    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     CustomUserDetailsService customUserDetailsService,
                                     JwtConfig jwtConfig, JwtSecretKey secretKey,
                                     @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver, JwtUtils jwtUtils
    ) {
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;


        this.resolver = resolver;
        this.jwtUtils = jwtUtils;

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //.addFilterBefore(authenticationFilter(), JwtEmailAndPasswordAuthenticationFilter.class)
                .addFilter(authenticationFilter())
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, secretKey, resolver), JwtEmailAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/error",
                        "/actuator/**",
                        "/api/testlog",
                        "/api/createUser",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**"
                ).permitAll()
                .antMatchers("/ocr_api/perform").hasRole("USER")

                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers("/changePassword").permitAll()
                .antMatchers(DICTIONARY_PROJECT_PATH + "body").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(this::logoutSuccessHandler)
        ;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customDaoAuthenticationProvider());
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.applyPermitDefaultValues();
        corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));

        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public CustomDaoAuthenticationProvider customDaoAuthenticationProvider() {
        CustomDaoAuthenticationProvider authenticationProvider = new CustomDaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        System.out.println("Using my custom DaoAuthenticationProvider");
        return authenticationProvider;
    }

    @Bean
    public JwtEmailAndPasswordAuthenticationFilter authenticationFilter() throws Exception {
        JwtEmailAndPasswordAuthenticationFilter authenticationFilter
                = new JwtEmailAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey, jwtUtils, resolver);
//        authenticationFilter.setAuthenticationSuccessHandler(this::loginSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(this::loginFailureHandler);
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationFilter;
    }




    private void loginFailureHandler(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException e) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        resolver.resolveException(request, response, null, e);
    }

    private void logoutSuccessHandler(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        response.setStatus(HttpStatus.OK.value());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), "Bye!");
    }
}
