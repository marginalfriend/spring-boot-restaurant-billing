package io.abun.wmb.TransactionService;

import io.abun.wmb.Constants.Routes;
import io.abun.wmb.TransactionService.dto.BillRequest;
import io.abun.wmb.TransactionService.dto.BillResponse;
import io.abun.wmb.TransactionService.interfaces.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Routes.ROOT + Routes.BILLS)
public class BillController {
    @Autowired
    BillService service;

    @GetMapping
    public List<BillResponse> findAll() {
        return service.findAll();
    }

    @PostMapping
    public BillResponse create(@RequestBody BillRequest bill) {
        return service.create(bill);
    }
}
