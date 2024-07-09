package com.security.gateway.auth;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final ApplicationUserDao applicationUserDao;


    public CustomUserDetailsService(@Qualifier("realdatabase") ApplicationUserDao applicationUserDao) {
        this.applicationUserDao = applicationUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return applicationUserDao.selectApplicationUserByEmail(email.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Email %s not found", email)));
    }

}
