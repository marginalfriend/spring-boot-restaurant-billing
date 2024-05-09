package io.abun.wmb.CustomerManagement;

import io.abun.wmb.Constants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_URL_ROOT + Constants.CUST_ENDPOINT)
public class CustomerController {
    CustomerService service;
    @PostMapping
    public Customer create(Customer customer) {
        return service.create(customer);
    }
}
