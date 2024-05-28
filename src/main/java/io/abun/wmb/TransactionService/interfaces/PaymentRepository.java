package io.abun.wmb.TransactionService.interfaces;

import io.abun.wmb.TransactionService.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
}
