package io.abun.wmb.MenuManagement;

import java.util.UUID;

public record Menu(
        Integer id,
        String  name,
        Integer price
) {
}
