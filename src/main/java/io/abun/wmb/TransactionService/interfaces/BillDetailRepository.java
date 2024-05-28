package io.abun.wmb.TransactionService.interfaces;

import io.abun.wmb.TransactionService.BillDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetailEntity, Integer>, JpaSpecificationExecutor<BillDetailEntity> {}