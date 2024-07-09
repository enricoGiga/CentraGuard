package com.security.gateway.service;

import com.security.gateway.dto.UserRoleDTO;
import com.security.gateway.repository.UserRoleRepository;
import com.security.gateway.entity.UserRole;
import com.security.gateway.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserRoleMapper userRoleMapper;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, UserRoleMapper userRoleMapper) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public List<UserRoleDTO> saveAll(List<UserRoleDTO> usersDTO) {
        List<UserRole> userRoles = userRoleRepository.saveAll(userRoleMapper.mapAllDTO(usersDTO));

        return userRoleMapper.mapAll(userRoles);
    }

    @Override
    public UserRoleDTO save(UserRoleDTO userRoleDTO) {

        UserRole userRole = userRoleRepository.save(userRoleMapper.map(userRoleDTO));
        return userRoleMapper.map(userRole);
    }

    @Override
    public UserRoleDTO findById(Integer uuid) {
        if (userRoleRepository.existsById(uuid)) {

            UserRole consumer = userRoleRepository.findById(uuid).get();
            return userRoleMapper.map(consumer);
        } else return null;
    }


    @Override
    public List<UserRoleDTO> findAll() {
        List<UserRole> roles = userRoleRepository.findAll();
        return userRoleMapper.mapAll(roles);
    }

    @Override
    public void addRoleToUser(String email, Integer userRoleId) {
        userRoleRepository.addRoleToUser(email, userRoleId);

    }
}
