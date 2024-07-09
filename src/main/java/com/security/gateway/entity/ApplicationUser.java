package com.security.gateway.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application_user")
@Entity
public class ApplicationUser {

    @Id
    @Column(unique=true,  columnDefinition = "VARCHAR(60) CHECK (length(email) >= 5)")
    private String email;
    @Column(nullable = false,  columnDefinition = "VARCHAR(60) CHECK (length(password) >= 6)")
    private String password;
    @Column(nullable = false,  columnDefinition = "VARCHAR(60) CHECK (length(first_name) >= 2)")
    private String firstName;
    @Column(nullable = false,  columnDefinition = "VARCHAR(60) CHECK (length(last_name) >= 2)")
    private String lastName;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "application_user_user_role",
            joinColumns = {@JoinColumn(name = "email")},
            inverseJoinColumns = {@JoinColumn(name = "user_role_id")})
    private Set<UserRole> userRoles;

    @OneToOne(mappedBy = "applicationUser", cascade = CascadeType.REMOVE)
    @PrimaryKeyJoinColumn
    private ResetPasswordCode resetPasswordCode;
}
