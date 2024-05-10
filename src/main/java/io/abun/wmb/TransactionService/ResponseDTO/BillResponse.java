package io.abun.wmb.TransactionService.ResponseDTO;

import io.abun.wmb.TransactionService.TransactionType;

import java.util.List;
import java.util.UUID;

public record BillResponse(
        UUID                        id,
        UUID                        customerId,
        String                      customerName,
        String                      customerPhone,
        TransactionType             transactionType,
        String                      table,
        List<BillDetailResponse>    billDetails
) {}