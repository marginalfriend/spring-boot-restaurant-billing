package io.abun.wmb.Auth.dto;

public record AuthRequest(
        String username,
        String password
) {
}
