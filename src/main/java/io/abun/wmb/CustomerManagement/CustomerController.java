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
    public List<Customer> find(@RequestParam UUID id, @RequestParam String name, @RequestParam String phone, @RequestParam Boolean isMember) {
        return service.findAll(new Customer(
                id,
                name,
                phone,
                isMember
        ));
    }
}
