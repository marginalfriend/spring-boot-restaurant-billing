package io.abun.wmb.TransactionService.RequestDTO;

import java.util.UUID;

public record BillDetailRequest(
        UUID menuId,
        Integer quantity
) {}