package io.abun.wmb.CustomerManagement;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    Customer create(Customer customer);
    List<Customer> findAll(Customer customer);
    Customer findById(UUID id);
    Customer update(Customer customer);
}
