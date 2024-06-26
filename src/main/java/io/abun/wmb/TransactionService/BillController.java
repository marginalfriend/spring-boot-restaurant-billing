package io.abun.wmb.TransactionService;

import io.abun.wmb.Constants.CommonResponse;
import io.abun.wmb.Constants.Messages;
import io.abun.wmb.Constants.Routes;
import io.abun.wmb.TransactionService.dto.BillRequest;
import io.abun.wmb.TransactionService.dto.BillResponse;
import io.abun.wmb.TransactionService.dto.payment.PaymentStatusUpdateRequest;
import io.abun.wmb.TransactionService.interfaces.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Routes.BILLS)
@RequiredArgsConstructor
public class BillController {
    private final BillService service;

    @GetMapping
    public List<BillResponse> findAll() {
        return service.findAll();
    }

    @PostMapping
    public BillResponse create(@RequestBody BillRequest bill) {
        return service.create(bill);
    }

    @PostMapping(path = Routes.PAYMENT_NOTIFICATION)
    public ResponseEntity<CommonResponse<?>> statusUpdate(
            @RequestBody Map<String, String> request
            ) {

        PaymentStatusUpdateRequest statusUpdateRequest = new PaymentStatusUpdateRequest(
                request.get("order_id"),
                request.get("transaction_status")
        );

        service.statusUpdate(statusUpdateRequest);

        return ResponseEntity.ok(
                CommonResponse.builder().statusCode(HttpStatus.OK.value()).message(Messages.UPDATED).build()
        );
    }
}
