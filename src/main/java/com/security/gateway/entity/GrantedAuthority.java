package com.security.gateway.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "granted_authority")
public class GrantedAuthority {
    @Id
    private Integer grantedAuthorityId;

    @Column(nullable = false, length = 20)
    private String authority;

    @ManyToOne
    @JoinColumn(name = "user_role_id")
    private UserRole userRole;

}
