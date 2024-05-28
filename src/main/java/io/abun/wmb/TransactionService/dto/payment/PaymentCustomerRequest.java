package io.abun.wmb.TransactionService.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentCustomerRequest(
        @JsonProperty(namespace = "first_name")
        String name
) {
}
