package com.security.gateway.auth;


import com.security.gateway.dto.ApplicationUserDTO;
import com.security.gateway.entity.ApplicationUser;
import com.security.gateway.mapper.ApplicationUserMapper;
import com.security.gateway.service.GrantedAuthorityServiceImpl;
import com.security.gateway.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository("realdatabase")
public class ApplicationUserDaoImpl implements ApplicationUserDao {
    private final UserService applicationUserService;
    private final GrantedAuthorityServiceImpl grantedAuthorityService;
    private final ApplicationUserMapper applicationUserMapper;

    public ApplicationUserDaoImpl(
            UserService applicationUserService,
            GrantedAuthorityServiceImpl grantedAuthorityService,
            ApplicationUserMapper applicationUserMapper) {
        this.applicationUserService = applicationUserService;
        this.grantedAuthorityService = grantedAuthorityService;
        this.applicationUserMapper = applicationUserMapper;
    }

    @Override
    public Optional<ApplicationUserDetails> selectApplicationUserByEmail(String email) {

        Optional<ApplicationUserDTO> userByEmail = applicationUserService.findUserByEmail(email);


        Set<String> allGrantedAuthorities = grantedAuthorityService.findAllGrantedAuthoritiesWithEmail(email);

        Set<? extends GrantedAuthority> grantedAuthorities = allGrantedAuthorities.stream()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        Optional<ApplicationUserDetails> optUserDetails = Optional.empty();
        if (userByEmail.isPresent()) {
            ApplicationUserDTO userDTO = userByEmail.get();
            ApplicationUserDetails userDetails = mapUserDetails(userDTO, grantedAuthorities);
            optUserDetails = Optional.of(userDetails);
        }

        return optUserDetails;
    }
    public ApplicationUserDetails mapUserDetails(ApplicationUserDTO userDTO, Set<? extends GrantedAuthority> grantedAuthorities) {
        ApplicationUser applicationUser = applicationUserMapper.map(userDTO);
        return new ApplicationUserDetails(
                applicationUser,
                grantedAuthorities
        );
    }

}
