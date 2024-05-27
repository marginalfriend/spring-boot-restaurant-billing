package io.abun.wmb.TransactionService;

import io.abun.wmb.CustomerManagement.Customer;
import io.abun.wmb.CustomerManagement.CustomerEntity;
import io.abun.wmb.CustomerManagement.CustomerService;
import io.abun.wmb.MenuManagement.MenuEntity;
import io.abun.wmb.MenuManagement.interfaces.MenuService;
import io.abun.wmb.TableManagement.TableEntity;
import io.abun.wmb.TableManagement.TableRecord;
import io.abun.wmb.TableManagement.TableService;
import io.abun.wmb.TransactionService.RequestDTO.BillRequest;
import io.abun.wmb.TransactionService.ResponseDTO.BillDetailResponse;
import io.abun.wmb.TransactionService.ResponseDTO.BillResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BillServiceImpl implements BillService{
    @Autowired
    BillRepository          billRepository;
    @Autowired
    CustomerService         customerService;
    @Autowired
    MenuService             menuService;
    @Autowired
    TableService            tableService;

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
                    MenuEntity menu = MenuEntity.parse(menuService.findById(detail.menuId()));

                    return BillDetailEntity.builder()
                            .quantity(detail.quantity())
                            .menu(menu)
                            .bill(bill)
                            .build();

                }).toList();

        // We only need to flush bill since bill details will be cascaded
        bill.setBillDetails(billDetails);
        billRepository.saveAndFlush(bill);

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
                customer.id(),
                customer.name(),
                customer.phone(),
                bill.getTransactionType(),
                bill.getTable().getName(),
                detailsResponse
        );
    }

    @Transactional(readOnly = true)
    @Override
    public BillResponse findById(UUID id) {
        return null;
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

            billResponses.add(
                new BillResponse(
                        e.getId(),
                        customer.id(),
                        customer.name(),
                        customer.phone(),
                        e.getTransactionType(),
                        e.getTable().getName(),
                        billDetails
                )
            );
        });

        return billResponses;
    }
}
