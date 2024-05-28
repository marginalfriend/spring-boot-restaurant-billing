package io.abun.wmb.TransactionService.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentDetailRequest(
        @JsonProperty("gross_amount")
        Long amount,

        @JsonProperty("order_id")
        String orderId
) {
}
