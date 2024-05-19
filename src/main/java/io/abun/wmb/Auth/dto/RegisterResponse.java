package io.abun.wmb.Auth.dto;

import java.util.List;

public record RegisterResponse(
        String userName,
        List<String> roles
) {
}
