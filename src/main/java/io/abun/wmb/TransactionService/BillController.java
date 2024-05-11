package io.abun.wmb.TransactionService;

import io.abun.wmb.Constants;
import io.abun.wmb.TransactionService.RequestDTO.BillRequest;
import io.abun.wmb.TransactionService.ResponseDTO.BillResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.API_URL_ROOT + Constants.BILL_ENDPOINT)
public class BillController {
    BillService service;

    @GetMapping
    public List<BillResponse> findAll() {
        return null;
    }

    @PostMapping
    public BillResponse create(@RequestBody BillRequest bill) {
        return service.create(bill);
    }
}
