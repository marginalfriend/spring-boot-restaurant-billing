package io.abun.wmb.TransactionService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetailEntity, Integer>, JpaSpecificationExecutor<BillDetailEntity> {}