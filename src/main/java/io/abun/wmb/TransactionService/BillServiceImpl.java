package io.abun.wmb.TransactionService;

import io.abun.wmb.CustomerManagement.Customer;
import io.abun.wmb.CustomerManagement.CustomerEntity;
import io.abun.wmb.CustomerManagement.CustomerService;
import io.abun.wmb.MenuManagement.MenuEntity;
import io.abun.wmb.MenuManagement.MenuService;
import io.abun.wmb.TableManagement.TableEntity;
import io.abun.wmb.TableManagement.TableService;
import io.abun.wmb.TransactionService.RequestDTO.BillRequest;
import io.abun.wmb.TransactionService.ResponseDTO.BillDetailResponse;
import io.abun.wmb.TransactionService.ResponseDTO.BillResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    BillDetailRepository    billDetailRepository;
    @Autowired
    CustomerService         customerService;
    @Autowired
    MenuService             menuService;
    @Autowired
    TableService            tableService;

    @Override
    @Transactional
    public BillResponse create(BillRequest request) {
        // Looking for customer
        Customer customer = customerService.findById(request.customerId());

        // Inserting customer into BillEntity
        BillEntity bill = BillEntity.builder()
                .customer(CustomerEntity.parse(customer))
                .transactionType(request.transactionType())
                .transDate(Timestamp.valueOf(LocalDateTime.now()))
                .table(TableEntity.parse(tableService.findById(request.tableId())))
                .build();

        // Building BillDetailEntity(s) to then be inserted into BillEntity
        List<BillDetailEntity> billDetails = request.billDetails().stream()
                .map(detail -> {
                    MenuEntity menu = MenuEntity.parse(menuService.findById(detail.menuId()));

                    return BillDetailEntity.builder()
                            .menu(menu)
                            .quantity(detail.quantity())
                            .bill(bill)
                            .build();

                }).toList();

        // We only need to flush bill since bill details will be cascaded
        bill.setBillDetails(billDetails);
        billRepository.saveAndFlush(bill);

        // Building BillDetailResponse to then be sent to client
        List<BillDetailResponse> detailsResponse = billDetails.stream().map(
                detail -> new BillDetailResponse (
                        detail.getMenu().getName(),
                        detail.getMenu().getPrice(),
                        detail.getQuantity(),
                        detail.getQuantity() * detail.getMenu().getPrice()
                )
        ).toList();

        // Building BillResponse to send to client
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

    @Override
    public BillResponse findById(UUID id) {
        return null;
    }

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
