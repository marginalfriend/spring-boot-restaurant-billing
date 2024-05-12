package io.abun.wmb.TransactionService.RequestDTO;

import java.util.UUID;

public record BillDetailRequest(
        Integer menuId,
        Integer quantity
) {}