package io.abun.wmb.CustomerManagement;

import java.util.UUID;

public record Customer(
        UUID id,
        String name,
        String phone,
        Boolean isMember
) {
}
