package io.abun.wmb.Auth.interfaces;

import io.abun.wmb.Auth.UserAccountEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserAccountService extends UserDetailsService {
    UserAccountEntity loadUserById(UUID id);
    UserAccountEntity getByContext();
}
