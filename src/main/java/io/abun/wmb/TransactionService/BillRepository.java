package io.abun.wmb.TransactionService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface BillRepository extends JpaRepository<BillEntity, UUID>, JpaSpecificationExecutor<BillEntity> {
}
