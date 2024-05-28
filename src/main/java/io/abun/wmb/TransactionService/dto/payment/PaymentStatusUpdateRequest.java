package io.abun.wmb.TransactionService.dto.payment;

public record PaymentStatusUpdateRequest(
        String orderId,
        String transactionStatus
) {
}
