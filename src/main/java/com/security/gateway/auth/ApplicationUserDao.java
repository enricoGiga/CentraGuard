package com.security.gateway.auth;

import java.util.Optional;

public interface ApplicationUserDao {
     Optional<ApplicationUserDetails> selectApplicationUserByEmail(String email);
}
