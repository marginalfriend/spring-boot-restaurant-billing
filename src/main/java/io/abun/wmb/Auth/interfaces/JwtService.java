package io.abun.wmb.Auth.interfaces;

import io.abun.wmb.Auth.UserAccountEntity;
import io.abun.wmb.Auth.dto.JwtClaims;

public interface JwtService {
    String generateToken(UserAccountEntity userAccountEntity);

    boolean verifyJwtToken(String token);

    JwtClaims getClaimsByToken(String token);
}
