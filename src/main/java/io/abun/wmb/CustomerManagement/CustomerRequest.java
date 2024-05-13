package io.abun.wmb.CustomerManagement;

import io.abun.wmb.Constants.SortingDirection;

import java.util.UUID;

public record CustomerRequest (
        UUID                id,
        String              name,
        String              phone,
        Boolean             isMember,

        Integer             page,
        Integer             size,

        String              sortBy,
        SortingDirection    direction
) {
}
