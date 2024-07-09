package com.security.gateway.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reset_password_code")
@Entity
public class ResetPasswordCode {
    @Id
    @Column(name = "user_email")
    private String email;
    @Column(unique = true, columnDefinition = "integer check (reset_code <= 99999999)")
    private Integer resetCode;
    @Column( columnDefinition = "boolean default false")
    private boolean verified;

    @OneToOne()
    @MapsId
    @JoinColumn(name = "user_email")
    private ApplicationUser applicationUser;

}
