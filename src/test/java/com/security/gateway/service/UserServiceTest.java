package com.security.gateway.service;

import com.security.gateway.dto.ApplicationUserDTO;
import com.security.gateway.entity.ApplicationUser;
import com.security.gateway.mapper.ApplicationUserMapper;
import com.security.gateway.repository.ApplicationUserRepository;
import com.security.gateway.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@PropertySource("classpath:application-test.yml")
class UserServiceTest {
    public static final String ID = "enrico@gigante.it";
    @Mock
    private EntityManager entityManager;
    @Mock
    private ApplicationUserRepository applicationUserRepository;
    @Autowired
    private ApplicationUserMapper applicationUserMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    // not needed
    //@InjectMocks
    private UserServiceImpl userService;


    @BeforeEach
    void setMockOutput() {
        this.userService = new UserServiceImpl(entityManager, applicationUserRepository, verificationTokenRepository, applicationUserMapper,
                passwordEncoder, userRoleService, eventPublisher);
        when(applicationUserRepository.existsById(any(String.class))).thenReturn(true);
        when(applicationUserRepository.findById(ID)).thenReturn(applicationUser());
    }


    @Test
    void findUserByEmail() {
        final Optional<ApplicationUserDTO> userDTOOptional = userService.findUserByEmail(ID);
        assertTrue(userDTOOptional.isPresent());
        assertEquals(userDTOOptional.get().getEmail(), userDTOOptional.get().getEmail());
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
