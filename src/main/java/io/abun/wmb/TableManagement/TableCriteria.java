package io.abun.wmb.TableManagement;

public record TableCriteria(
        String  name,
        Integer minCap,
        Integer maxCap
) {
}
