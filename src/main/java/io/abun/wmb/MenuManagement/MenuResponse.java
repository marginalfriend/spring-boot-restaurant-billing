package io.abun.wmb.MenuManagement;

import io.abun.wmb.MenuManagement.ImageHandler.MenuImageResponse;

public record MenuResponse(
        Integer id,
        String name,
        Integer price,
        MenuImageResponse image
) {
}
