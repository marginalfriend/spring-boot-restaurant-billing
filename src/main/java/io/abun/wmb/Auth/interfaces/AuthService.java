package io.abun.wmb.Auth.interfaces;

import io.abun.wmb.Auth.dto.AuthRequest;
import io.abun.wmb.Auth.dto.LoginResponse;
import io.abun.wmb.Auth.dto.RegisterResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthService {
    RegisterResponse register(AuthRequest request);
    RegisterResponse registerAdmin(AuthRequest request);
    LoginResponse login(AuthRequest request);
    void initSuperAdmin();
}
