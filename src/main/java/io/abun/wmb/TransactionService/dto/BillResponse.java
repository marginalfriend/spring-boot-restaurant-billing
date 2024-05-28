package io.abun.wmb.TransactionService.dto;

import io.abun.wmb.CustomerManagement.Customer;
import io.abun.wmb.TransactionService.TransactionType;
import io.abun.wmb.TransactionService.dto.payment.PaymentResponse;

import java.util.List;
import java.util.UUID;

public record BillResponse(
        UUID                        id,
        String                      table,
        Customer                    customer,
        List<BillDetailResponse>    billDetails,
        TransactionType             transactionType,
        PaymentResponse             paymentResponse
) {}