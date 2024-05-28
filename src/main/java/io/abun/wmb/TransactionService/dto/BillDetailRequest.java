package io.abun.wmb.TransactionService.dto;

import java.util.UUID;

public record BillDetailRequest(
        Integer menuId,
        Integer quantity
) {}