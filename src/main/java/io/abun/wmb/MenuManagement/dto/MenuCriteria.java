package io.abun.wmb.MenuManagement.dto;

public record MenuCriteria(
        String name,
        Integer pmin,
        Integer pmax
) {
}
