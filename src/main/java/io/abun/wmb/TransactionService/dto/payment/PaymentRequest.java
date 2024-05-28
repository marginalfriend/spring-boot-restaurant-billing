package io.abun.wmb.TransactionService.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaymentRequest(
    @JsonProperty("transaction_details")
     PaymentDetailRequest paymentDetail,

    @JsonProperty("item_details")
    List<PaymentItemDetailRequest> paymentItemDetails,

    @JsonProperty("enabled_payments")
    List<String> paymentMethod,

    @JsonProperty("customer_details")
    PaymentCustomerRequest customer
) {
}
