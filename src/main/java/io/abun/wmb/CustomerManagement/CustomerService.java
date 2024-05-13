package io.abun.wmb.CustomerManagement;

import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CustomerService {
    Customer create(Customer customer);
    Page<Customer> findAll(CustomerRequest customer);
    Customer findById(UUID id);
    Customer update(Customer customer);
    void removeById(UUID id);
}
