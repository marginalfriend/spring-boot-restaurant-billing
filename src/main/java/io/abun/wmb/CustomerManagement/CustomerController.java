package io.abun.wmb.CustomerManagement;

import io.abun.wmb.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Constants.API_URL_ROOT + Constants.CUST_ENDPOINT)
public class CustomerController {
    @Autowired
    CustomerService service;
    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        Customer created = service.create(customer);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
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

    @PutMapping
    public ResponseEntity<Customer> update(@RequestBody Customer customer) {
        Customer updated = service.update(customer);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping
    public String delete(@RequestBody UUID id) {
        service.removeById(id);
        return "Removed a customer";
    }
}
