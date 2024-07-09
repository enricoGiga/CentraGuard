package com.security.gateway.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyIntegrityPasswordCode {
    String email;
    Integer code;
}
