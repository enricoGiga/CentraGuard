package com.security.gateway.mapper;

import com.security.gateway.dto.ApplicationUserDTO;
import com.security.gateway.entity.ApplicationUser;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ApplicationUserMapper {


    public ApplicationUserMapper() {

    }




    abstract public ApplicationUserDTO map(ApplicationUser consumer);

    @InheritInverseConfiguration
    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "resetPasswordCode", ignore = true)
    abstract public ApplicationUser map(ApplicationUserDTO applicationUserDTO);

    abstract public List<ApplicationUserDTO> mapAll(List<ApplicationUser> consumers);


    abstract public List<ApplicationUser> mapAllDTO(List<ApplicationUserDTO> consumers);
}
