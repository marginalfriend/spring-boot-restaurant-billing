package io.abun.wmb.MenuManagement;

import java.util.UUID;

public record Menu(
        UUID    id,
        String  name,
        Integer price
) {
}
