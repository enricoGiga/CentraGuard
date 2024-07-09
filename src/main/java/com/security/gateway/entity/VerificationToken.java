package com.security.gateway.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VerificationToken {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = ApplicationUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "email")
    private ApplicationUser user;

    private Date expiryDate;

    public VerificationToken(String token, ApplicationUser user, Date expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;

    }


    // standard constructors, getters and setters
}
