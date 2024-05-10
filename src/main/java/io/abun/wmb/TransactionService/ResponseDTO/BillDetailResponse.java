package io.abun.wmb.TransactionService.ResponseDTO;

public record BillDetailResponse(
        String      menuName,
        Integer     menuPrice,
        Integer     quantity,
        Integer     totalPrice
) {}