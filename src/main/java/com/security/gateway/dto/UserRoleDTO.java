package com.security.gateway.dto;


import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleDTO {

    private Integer userRoleId;

    private String role;

    private List<GrantedAuthorityDTO> grantedAuthorities;


}
