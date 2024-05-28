package io.abun.wmb.TransactionService.interfaces;

import io.abun.wmb.TransactionService.BillEntity;
import io.abun.wmb.TransactionService.PaymentEntity;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    PaymentEntity create(BillEntity bill);
}
