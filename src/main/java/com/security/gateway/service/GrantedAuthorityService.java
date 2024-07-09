package com.security.gateway.service;

import com.security.gateway.dto.GrantedAuthorityDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface GrantedAuthorityService {
    GrantedAuthorityDTO save(GrantedAuthorityDTO consumer);

    GrantedAuthorityDTO findById(Integer uuid);

    List<GrantedAuthorityDTO> findAll();

    List<GrantedAuthorityDTO> saveAll(ArrayList<GrantedAuthorityDTO> grantedAuthorityDTOS);


    Set<String> findAllGrantedAuthoritiesWithEmail(String email);
}
