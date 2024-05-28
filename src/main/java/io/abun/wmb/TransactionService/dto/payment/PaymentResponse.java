package io.abun.wmb.TransactionService.dto.payment;

public record PaymentResponse(
        String id,
        String token,
        String redirectUrl,
        String transactionStatus
) {
}
