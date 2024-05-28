package io.abun.wmb.TransactionService.dto;

public record BillDetailResponse(
        String      menuName,
        Integer     menuPrice,
        Integer     quantity,
        Integer     totalPrice
) {}