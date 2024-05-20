package io.abun.wmb.CustomerManagement;

import io.abun.wmb.Constants.CommonResponse;
import io.abun.wmb.Constants.PagingResponse;
import io.abun.wmb.Constants.Messages;
import io.abun.wmb.Constants.Routes;
import io.abun.wmb.Constants.SortingDirection;
import io.abun.wmb.ErrorHandler.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Routes.CUSTOMERS)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;
    private final ValidatorUtil validatorUtil;

//    @PostMapping
//    public ResponseEntity<CommonResponse<Customer>> create(@RequestBody Customer customer) {
//        validatorUtil.validate(customer);
//
//        Customer created = service.create(customer);
//
//        CommonResponse<Customer> response = CommonResponse.<Customer>builder()
//                .statusCode(HttpStatus.CREATED.value())
//                .message(Messages.CREATED)
//                .data(created)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<Customer>>> find(
            @RequestParam(name = "id",          required = false)   UUID    id,
            @RequestParam(name = "name",        required = false)   String  name,
            @RequestParam(name = "phone",       required = false)   String  phone,
            @RequestParam(name = "isMember",    required = false)   Boolean isMember,
            @RequestParam(name = "page",        required = false)   Integer page,
            @RequestParam(name = "size",        required = false)   Integer size,
            @RequestParam(name = "sortBy",      required = false)   String  sortBy,
            @RequestParam(name = "direction",   required = false)   String  direction
    ) {
        Page<Customer> customers = service.findAll(new CustomerRequest(
                id,
                name,
                phone,
                isMember,
                page,
                size,
                sortBy,
                SortingDirection.valueOf(direction)
        ));

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages     (customers.getTotalPages())
                .totalElements  (customers.getTotalElements())
                .page           (customers.getPageable().getPageNumber())
                .size           (customers.getPageable().getPageSize())
                .hasNext        (customers.hasNext())
                .hasPrevious    (customers.hasPrevious())
                .build();

        CommonResponse<List<Customer>> response = CommonResponse.<List<Customer>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(Messages.FOUND)
                .data(customers.getContent())
                .paging(pagingResponse)
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
