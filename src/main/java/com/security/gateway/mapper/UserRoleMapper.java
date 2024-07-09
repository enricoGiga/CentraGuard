package com.security.gateway.mapper;

import com.security.gateway.dto.UserRoleDTO;
import com.security.gateway.entity.UserRole;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    @Mapping(target = "userRoleId", source = "userRoleId")
    @Mapping(target = "role", source = "role")
    UserRoleDTO map(UserRole consumer);
    @InheritInverseConfiguration
    @Mapping(target = "applicationUsers", ignore = true)
    @Mapping(target = "grantedAuthorities", ignore = true)
    UserRole map(UserRoleDTO consumerDTO);

    List<UserRoleDTO> mapAll(List<UserRole> userRoles);

    List<UserRole> mapAllDTO(List<UserRoleDTO> userRoleDTOS);
}
