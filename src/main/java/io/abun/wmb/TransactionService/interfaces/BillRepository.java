package io.abun.wmb.TransactionService.interfaces;

import io.abun.wmb.TransactionService.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, UUID>, JpaSpecificationExecutor<BillEntity> {}