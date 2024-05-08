package io.abun.wmb.CustomerManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    CustomerRepository repository;

    @Override
    public Customer create(Customer customer) {
        return repository.save(CustomerEntity.parse(customer)).toRecord();
    }

    @Override
    public List<Customer> findAll() {
        return null;
    }

    @Override
    public Customer findById(UUID id) {
        return null;
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }
}
