package io.abun.wmb.TransactionService;

import io.abun.wmb.TransactionService.RequestDTO.BillRequest;
import io.abun.wmb.TransactionService.ResponseDTO.BillResponse;

import java.util.List;
import java.util.UUID;

public interface BillService {
    BillResponse create(BillRequest request);
    BillResponse findById(UUID id);
    List<BillResponse> findAll();
}