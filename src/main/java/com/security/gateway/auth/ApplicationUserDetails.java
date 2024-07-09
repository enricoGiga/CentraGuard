package com.security.gateway.auth;


import com.security.gateway.entity.ApplicationUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class ApplicationUserDetails implements UserDetails {
    private final ApplicationUser applicationUser;
    private final Set<? extends GrantedAuthority> grantedAuthorities;

    public ApplicationUserDetails(ApplicationUser applicationUser, Set<? extends GrantedAuthority> grantedAuthorities ) {
        this.applicationUser = applicationUser;
        this.grantedAuthorities = grantedAuthorities;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.applicationUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.applicationUser.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() {
        return this.applicationUser.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.applicationUser.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.applicationUser.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.applicationUser.isEnabled();
    }
}
