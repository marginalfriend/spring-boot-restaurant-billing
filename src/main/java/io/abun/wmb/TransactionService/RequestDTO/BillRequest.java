package io.abun.wmb.TransactionService.RequestDTO;

import java.util.List;
import java.util.UUID;

public record BillRequest(
        UUID                        customerId,
        List<BillDetailRequest>     billDetails
) {}