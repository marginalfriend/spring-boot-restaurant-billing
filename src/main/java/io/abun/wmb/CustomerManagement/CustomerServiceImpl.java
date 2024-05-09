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
        // The findAll method returns CustomerEntity, that's why we need to convert those into records first
        return repository.findAll().stream().map(CustomerEntity::toRecord).toList();
    }

    @Override
    public Customer findById(UUID id) {
        return repository.findById(id).orElseThrow().toRecord();
    }

    @Override
    public Customer update(Customer customer) {
        CustomerEntity toUpdate = repository.findById(customer.id()).orElse(null);
        assert toUpdate != null;

        toUpdate.setName(customer.name());
        toUpdate.setPhone(customer.phone());
        toUpdate.setIsMember(customer.isMember());

        return repository.saveAndFlush(toUpdate).toRecord();
    }
}
