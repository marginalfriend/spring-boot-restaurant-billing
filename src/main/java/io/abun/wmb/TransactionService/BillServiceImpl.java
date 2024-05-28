package io.abun.wmb.TransactionService;

import io.abun.wmb.Constants.Messages;
import io.abun.wmb.CustomerManagement.Customer;
import io.abun.wmb.CustomerManagement.CustomerEntity;
import io.abun.wmb.CustomerManagement.CustomerService;
import io.abun.wmb.MenuManagement.MenuEntity;
import io.abun.wmb.MenuManagement.interfaces.MenuService;
import io.abun.wmb.TableManagement.TableEntity;
import io.abun.wmb.TableManagement.TableRecord;
import io.abun.wmb.TableManagement.TableService;
import io.abun.wmb.TransactionService.dto.BillRequest;
import io.abun.wmb.TransactionService.dto.BillDetailResponse;
import io.abun.wmb.TransactionService.dto.BillResponse;
import io.abun.wmb.TransactionService.dto.payment.PaymentResponse;
import io.abun.wmb.TransactionService.dto.payment.PaymentStatusUpdateRequest;
import io.abun.wmb.TransactionService.interfaces.BillRepository;
import io.abun.wmb.TransactionService.interfaces.BillService;
import io.abun.wmb.TransactionService.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository    billRepository;

    private final CustomerService   customerService;
    private final MenuService       menuService;
    private final TableService      tableService;
    private final PaymentService    paymentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BillResponse create(BillRequest request) {
        Timestamp       now             = Timestamp.valueOf(LocalDateTime.now());
        Customer        customer        = customerService.findById(request.customerId());
        CustomerEntity  customerEntity  = CustomerEntity.parse(customer);
        TransactionType transactionType = request.transactionType();

        BillEntity bill = BillEntity.builder()
                .transactionType(transactionType)
                .customer(customerEntity)
                .transDate(now)
                .build();

        boolean isDineIn = transactionType == TransactionType.DI;
        if (isDineIn) {
            TableRecord tableRecord = tableService.findById(request.tableId());
            TableEntity.parse(tableRecord);
        }

        List<BillDetailEntity> billDetails = request.billDetails().stream()
                .map(detail -> {
                    MenuEntity menu = menuService.findEntityById(detail.menuId());

                    return BillDetailEntity.builder()
                            .quantity(detail.quantity())
                            .menu(menu)
                            .bill(bill)
                            .build();

                }).toList();

        bill.setBillDetails(billDetails);
        billRepository.saveAndFlush(bill);

        PaymentEntity payment = paymentService.create(bill);
        bill.setPayment(payment);

        PaymentResponse paymentResponse = new PaymentResponse(
                payment.getId().toString(),
                payment.getToken(),
                payment.getRedirectUrl(),
                payment.getTransactionStatus()
        );

        List<BillDetailResponse> detailsResponse = billDetails.stream().map(
                detail -> new BillDetailResponse (
                        detail.getMenu().getName(),
                        detail.getMenu().getPrice(),
                        detail.getQuantity(),
                        detail.getQuantity() * detail.getMenu().getPrice()
                )
        ).toList();

        return new BillResponse(
                bill.getId(),
                bill.getTable().getName(),
                customer,
                detailsResponse,
                bill.getTransactionType(),
                paymentResponse
        );
    }

    @Transactional(readOnly = true)
    @Override
    public BillResponse findById(UUID id) {
        BillEntity billEntity = billRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.NOT_FOUND + ": Bill not found")
        );

        PaymentResponse paymentResponse = new PaymentResponse(
                billEntity.getPayment().getId().toString(),
                billEntity.getPayment().getToken(),
                billEntity.getPayment().getRedirectUrl(),
                billEntity.getPayment().getTransactionStatus()
        );

        Customer customerResponse = customerService.findById(billEntity.getCustomer().getId());

        List<BillDetailResponse> billDetailResponses = billEntity.getBillDetails().stream().map(
                detail -> new BillDetailResponse(
                        detail.getMenu().getName(),
                        detail.getMenu().getPrice(),
                        detail.getQuantity(),
                        detail.getQuantity() * detail.getMenu().getPrice()
                )
        ).toList();

        return new BillResponse(
                billEntity.getId(),
                billEntity.getTable().getName(),
                customerResponse,
                billDetailResponses,
                billEntity.getTransactionType(),
                paymentResponse
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<BillResponse> findAll() {
        List<BillEntity>    billEntities    = billRepository.findAll();
        List<BillResponse>  billResponses   = new ArrayList<>();

        billEntities.forEach(e -> {
            Customer customer = e.getCustomer().toRecord();

            List<BillDetailResponse> billDetails = e.getBillDetails().stream().map(
                    detail -> new BillDetailResponse (
                            detail.getMenu().getName(),
                            detail.getMenu().getPrice(),
                            detail.getQuantity(),
                            detail.getQuantity() * detail.getMenu().getPrice()
                    )
            ).toList();

            PaymentResponse paymentResponse = new PaymentResponse(
                    e.getPayment().getId().toString(),
                    e.getPayment().getToken(),
                    e.getPayment().getRedirectUrl(),
                    e.getPayment().getTransactionStatus()
            );

            billResponses.add(
                new BillResponse(
                        e.getId(),
                        e.getTable().getName(),
                        customer,
                        billDetails,
                        e.getTransactionType(),
                        paymentResponse
                )
            );
        });

        return billResponses;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void statusUpdate(PaymentStatusUpdateRequest paymentStatusUpdateRequest) {
        BillEntity bill = billRepository.findById(UUID.fromString(paymentStatusUpdateRequest.orderId())).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.NOT_FOUND + ": Bill not found")
        );

        PaymentEntity payment = bill.getPayment();
        payment.setTransactionStatus(paymentStatusUpdateRequest.transactionStatus());
    }
}
