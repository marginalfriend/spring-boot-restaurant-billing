package io.abun.wmb.TransactionService.interfaces;

import io.abun.wmb.TransactionService.dto.BillRequest;
import io.abun.wmb.TransactionService.dto.BillResponse;
import io.abun.wmb.TransactionService.dto.payment.PaymentStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface BillService {
    BillResponse create(BillRequest request);
    BillResponse findById(UUID id);
    List<BillResponse> findAll();

    void statusUpdate(PaymentStatusUpdateRequest paymentStatusUpdateRequest);
}