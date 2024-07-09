package com.security.gateway.mapper;

import com.security.gateway.dto.GrantedAuthorityDTO;
import com.security.gateway.entity.GrantedAuthority;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GrantedAuthorityMapper {


    @Mapping(target = "authority", source = "authority")
    @Mapping(target = "grantedAuthorityId", source = "grantedAuthorityId")
    GrantedAuthorityDTO map(GrantedAuthority consumer);

    @InheritInverseConfiguration
    @Mapping(target = "userRole", ignore = true)
    GrantedAuthority map(GrantedAuthorityDTO consumerDTO);

    List<GrantedAuthorityDTO> mapAll(List<GrantedAuthority> consumers);
    List<GrantedAuthority> mapAllDTO(List<GrantedAuthorityDTO> consumers);



}
