package io.abun.wmb.Auth.dto;

import java.util.List;

public record JwtClaims(
        String userAccountId,
        List<String> roles
) {
}
