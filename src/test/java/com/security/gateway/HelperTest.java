package com.security.gateway;

import com.security.gateway.dto.ApplicationUserDTO;
import com.security.gateway.entity.ApplicationUser;

import java.util.Optional;

public class HelperTest {
    public ApplicationUserDTO applicationUserDTO() {
        return ApplicationUserDTO.builder().email("enrico@gmail.com")
                .firstName("enrico")
                .lastName("gigante")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .password("password")
                .build();
    }
    public Optional<ApplicationUser> applicationUser() {
        return Optional.of(ApplicationUser.builder().email("enrico@gmail.com")
                .firstName("enrico")
                .lastName("gigante")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .password("password")
                .build());
    }
}
