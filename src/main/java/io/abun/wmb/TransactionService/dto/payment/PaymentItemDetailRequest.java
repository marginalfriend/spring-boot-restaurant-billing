package io.abun.wmb.TransactionService.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentItemDetailRequest(
        @JsonProperty("quantity")
        Integer quantity,

        @JsonProperty("price")
        Integer price,

        @JsonProperty("name")
        String name
) {
}
