package com.security.gateway.service;

import com.security.gateway.dto.UserRoleDTO;

import java.util.List;

public interface UserRoleService {
    List<UserRoleDTO> saveAll(List<UserRoleDTO> usersDTO);
    UserRoleDTO save(UserRoleDTO consumer);

    UserRoleDTO findById(Integer uuid);

    List<UserRoleDTO> findAll();

    void addRoleToUser(
            String email,  Integer userRoleId);
}
