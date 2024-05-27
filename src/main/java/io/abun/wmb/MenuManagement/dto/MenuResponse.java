package io.abun.wmb.MenuManagement.dto;

import io.abun.wmb.ImageHandler.dto.ImageResponse;

public record MenuResponse(
        Integer id,
        String name,
        Integer price,
        ImageResponse image
) {
}
