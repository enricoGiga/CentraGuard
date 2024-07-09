package com.security.gateway.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_role")
public class UserRole {
    @Id
    private Integer userRoleId;

    @Column(nullable = false, length = 20)
    private String role;

    @ManyToMany(mappedBy = "userRoles")
    Set<ApplicationUser> applicationUsers;

    @OneToMany(mappedBy="userRole", cascade = CascadeType.REMOVE)
    private Set<GrantedAuthority> grantedAuthorities;
}
