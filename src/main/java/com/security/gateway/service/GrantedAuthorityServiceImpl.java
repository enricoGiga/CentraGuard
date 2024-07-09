package com.security.gateway.service;

import com.security.gateway.dto.GrantedAuthorityDTO;
import com.security.gateway.entity.GrantedAuthority;
import com.security.gateway.repository.GrantedAuthorityRepository;
import com.security.gateway.mapper.GrantedAuthorityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GrantedAuthorityServiceImpl implements GrantedAuthorityService {
    private final GrantedAuthorityRepository grantedAuthorityRepository;
    private final GrantedAuthorityMapper grantedAuthorityMapper;

    public GrantedAuthorityServiceImpl(GrantedAuthorityRepository grantedAuthorityRepository, GrantedAuthorityMapper grantedAuthorityMapper) {
        this.grantedAuthorityRepository = grantedAuthorityRepository;

        this.grantedAuthorityMapper = grantedAuthorityMapper;
    }


    @Override
    public GrantedAuthorityDTO save(GrantedAuthorityDTO grantedAuthorityDTO) {
        GrantedAuthority grantedAuthority = grantedAuthorityRepository.save(grantedAuthorityMapper.map(grantedAuthorityDTO));
        return grantedAuthorityMapper.map(grantedAuthority);
    }

    @Override
    public GrantedAuthorityDTO findById(Integer uuid) {
        if (grantedAuthorityRepository.existsById(uuid)) {

            GrantedAuthority consumer = grantedAuthorityRepository.findById(uuid).get();
            return grantedAuthorityMapper.map(consumer);
        } else return null;
    }


    @Override
    public List<GrantedAuthorityDTO> findAll() {
        List<GrantedAuthority> roles = grantedAuthorityRepository.findAll();
        return grantedAuthorityMapper.mapAll(roles);
    }

    @Override
    public List<GrantedAuthorityDTO> saveAll(ArrayList<GrantedAuthorityDTO> grantedAuthorityDTOS) {
        List<GrantedAuthority> grantedAuthorities = grantedAuthorityMapper.mapAllDTO(grantedAuthorityDTOS);
        return grantedAuthorityMapper.mapAll(grantedAuthorities);
    }



    @Override
    @Transactional
    public Set<String> findAllGrantedAuthoritiesWithEmail(String email) {
        return grantedAuthorityRepository.findAllGrantedAuthoritiesWithEmail(email);
    }
}
