package io.abun.wmb.CustomerManagement;

import io.abun.wmb.Constants;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Constants.API_URL_ROOT + Constants.CUST_ENDPOINT)
public class CustomerController {
    CustomerService service;
    @PostMapping
    public Customer create(Customer customer) {
        return service.create(customer);
    }

    @GetMapping
    public List<Customer> find(
            @RequestParam(name = "id",          required = false)   UUID id,
            @RequestParam(name = "name",        required = false)   String name,
            @RequestParam(name = "phone",       required = false)   String phone,
            @RequestParam(name = "isMember",    required = false)   Boolean isMember
    ) {
        return service.findAll(new Customer(
                id,
                name,
                phone,
                isMember
        ));
    }


}
