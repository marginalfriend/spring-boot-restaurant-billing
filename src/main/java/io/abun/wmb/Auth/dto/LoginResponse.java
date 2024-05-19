package io.abun.wmb.Auth.dto;

import java.util.List;

public record LoginResponse(
        String userName,
        String token,
        List<String> roles
) {
}
