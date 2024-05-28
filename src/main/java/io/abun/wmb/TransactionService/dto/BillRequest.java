package io.abun.wmb.TransactionService.dto;

import io.abun.wmb.TransactionService.TransactionType;

import java.util.List;
import java.util.UUID;

public record BillRequest(
        UUID                        customerId,
        TransactionType             transactionType,
        Integer                     tableId,
        List<BillDetailRequest>     billDetails
) {}