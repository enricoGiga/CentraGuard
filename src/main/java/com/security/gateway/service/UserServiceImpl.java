package com.security.gateway.service;

import com.security.gateway.dto.ApplicationUserDTO;
import com.security.gateway.entity.ApplicationUser;
import com.security.gateway.entity.VerificationToken;
import com.security.gateway.exception.EntityException;
import com.security.gateway.exception.VerifyTokenException;
import com.security.gateway.helper.DateSupport;
import com.security.gateway.mapper.ApplicationUserMapper;
import com.security.gateway.repository.ApplicationUserRepository;
import com.security.gateway.repository.VerificationTokenRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final ApplicationUserRepository applicationUserRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ApplicationUserMapper applicationUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    private final ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(EntityManager entityManager, ApplicationUserRepository applicationUserRepository,
                           VerificationTokenRepository verificationTokenRepository, ApplicationUserMapper applicationUserMapper, PasswordEncoder passwordEncoder,
                           UserRoleService userRoleService, ApplicationEventPublisher eventPublisher) {
        this.entityManager = entityManager;
        this.applicationUserRepository = applicationUserRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.applicationUserMapper = applicationUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRoleService = userRoleService;

        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ApplicationUserDTO save(ApplicationUserDTO applicationUserDTO) {
        applicationUserDTO.setPassword(passwordEncoder.encode(applicationUserDTO.getPassword()));
        ApplicationUser grantedAuthority = applicationUserRepository.save(applicationUserMapper.map(applicationUserDTO));
        return applicationUserMapper.map(grantedAuthority);
    }

    @Override
    @Transactional
    public Optional<ApplicationUserDTO>
    findUserByEmail(String email) {
        Optional<ApplicationUser> userOptional = applicationUserRepository.findById(email);
        if (userOptional.isPresent()) {

            ApplicationUser consumer = userOptional.get();
            return Optional.of(applicationUserMapper.map(consumer));
        } else return Optional.empty();
    }

    @Override
    @Transactional
    public List<ApplicationUserDTO> findAll() {
        List<ApplicationUser> repositoryAll = applicationUserRepository.findAll();
        return applicationUserMapper.mapAll(repositoryAll);
    }

    @Transactional
    public void insertNewApplicationUserName(ApplicationUserDTO applicationUserDTO, HttpServletRequest request) {
        if (applicationUserDTO == null) {
            throw new IllegalArgumentException("ApplicationUserDTO is null");
        }

        if (applicationUserDTO.getEmail() == null) {
            throw new IllegalArgumentException("Email is null");
        }

        if (applicationUserDTO.getPassword() == null) {
            throw new EntityException("Password is null");
        }

        if (applicationUserDTO.getFirstName() == null) {
            throw new EntityException("First name is null");
        }

        if (applicationUserDTO.getLastName() == null) {
            throw new EntityException("Last name is null");
        }


        applicationUserDTO.setEmail(applicationUserDTO.getEmail().toLowerCase());

        if (applicationUserRepository.existsById(applicationUserDTO.getEmail())) {
            throw new EntityException("email already exists");
        } else if (applicationUserDTO.getEmail().length() < 5) {
            throw new EntityException("email length too short ");
        } else if (applicationUserDTO.getPassword().length() < 6) {
            throw new EntityException("password length too short ");
        } else if (applicationUserDTO.getFirstName().length() < 2) {
            throw new EntityException("first name length too short ");
        } else if (applicationUserDTO.getLastName().length() < 2) {
            throw new EntityException("last name length too short ");
        }


        ApplicationUser applicationUser = ApplicationUser.builder()
                .email(applicationUserDTO.getEmail())
                .password(this.passwordEncoder.encode(applicationUserDTO.getPassword()))
                .firstName(applicationUserDTO.getFirstName())
                .lastName(applicationUserDTO.getLastName())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                // set enable false when you have implemented an email check system
                .isEnabled(true)
                .build();
        this.entityManager.persist(applicationUser);

        userRoleService.addRoleToUser(applicationUserDTO.getEmail(), 2);


    }

    @Override
    @Transactional
    public void createVerificationToken(ApplicationUserDTO user, String token) {
        VerificationToken myToken = new VerificationToken(token, applicationUserMapper.map(user),
                DateSupport.calculateExpiryDate());
        verificationTokenRepository.save(myToken);
    }

    @Override
    @Transactional
    public VerificationToken findVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public String confirmRegistration(String token) {
        VerificationToken verificationToken = findVerificationToken(token);
        if (verificationToken == null) {
            throw new VerifyTokenException("verification token not present");
        }
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new VerifyTokenException("verification token expired");
        }
        ApplicationUser user = verificationToken.getUser();
        user.setEnabled(true);
        applicationUserRepository.updateUserToEnabled(user.getEmail());
        return user.getFirstName();
    }


}
