package io.abun.wmb.TransactionService.RequestDTO;

import io.abun.wmb.TableManagement.TableRecord;
import io.abun.wmb.TransactionService.TransactionType;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record BillRequest(
        UUID                        customerId,
        List<BillDetailRequest>     billDetails,
        TransactionType             transactionType,
        TableRecord                 table
) {}