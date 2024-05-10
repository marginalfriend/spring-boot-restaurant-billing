package io.abun.wmb.TransactionService.ResponseDTO;

import java.util.List;
import java.util.UUID;

public record BillResponse(
        UUID                        id,
        String                      customerName,
        String                      customerPhone,
        List<BillDetailResponse>    billDetails
) {}