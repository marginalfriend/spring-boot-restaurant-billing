package io.abun.wmb.CustomerManagement;

import io.abun.wmb.CommonResponse.CommonResponse;
import io.abun.wmb.Constants.Messages;
import io.abun.wmb.Constants.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Routes.ROOT + Routes.CUST_ENDPOINT)
public class CustomerController {
    @Autowired
    CustomerService service;
    @PostMapping
    public ResponseEntity<CommonResponse<Customer>> create(@RequestBody Customer customer) {
        Customer created = service.create(customer);

        CommonResponse<Customer> response = CommonResponse.<Customer>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(Messages.CREATED)
                .data(created)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<Customer>>> find(
            @RequestParam(name = "id",          required = false)   UUID id,
            @RequestParam(name = "name",        required = false)   String name,
            @RequestParam(name = "phone",       required = false)   String phone,
            @RequestParam(name = "isMember",    required = false)   Boolean isMember
    ) {
        List<Customer> customers = service.findAll(new Customer(
                id,
                name,
                phone,
                isMember
        ));

        CommonResponse<List<Customer>> response = CommonResponse.<List<Customer>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(Messages.FOUND)
                .data(customers)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
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
