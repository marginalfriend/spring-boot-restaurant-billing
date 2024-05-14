package io.abun.wmb.CustomerManagement;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record Customer(
        UUID    id,
        @NotBlank(message = "Name is required")
        String  name,
        @NotBlank(message = "Phone number is required")
        String  phone,
        @NotBlank(message = "Membership state is required")
        Boolean isMember
) {
}
