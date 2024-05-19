package io.abun.wmb.Auth.interfaces;

import io.abun.wmb.Auth.UserAccount;
import io.abun.wmb.Auth.dto.JwtClaims;

public interface JwtService {
    String generateToken(UserAccount userAccount);

    boolean verifyJwtToken(String token);

    JwtClaims getClaimsByToken(String token);
}
