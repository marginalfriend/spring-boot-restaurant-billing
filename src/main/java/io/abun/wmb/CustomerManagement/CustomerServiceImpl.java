package io.abun.wmb.CustomerManagement;

import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
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
    public List<Customer> findAll(Customer customer) {
        if (customer == null) {
            return repository.findAll().stream().map(CustomerEntity::toRecord).toList();
        }

        Specification<CustomerEntity> specifications = CustomerSpecification.getSpecification(customer);

        List<Customer> result = new ArrayList<>();

        resultShooter(result, repository.findAll(specifications));

        return result;
    }

    @Override
    public Customer findById(UUID id) {
        return repository.findById(id).orElseThrow().toRecord();
    }

    @Override
    public Customer update(Customer customer) {

        CustomerEntity toUpdate = repository.findById(customer.id()).orElse(null);
        assert toUpdate != null;

        if (customer.name() != null) {
            toUpdate.setName(customer.name());
        }

        if (customer.phone() != null) {
            toUpdate.setPhone(customer.phone());
        }

        if (customer.isMember() != null) {
            toUpdate.setIsMember(customer.isMember());
        }

        return repository.saveAndFlush(toUpdate).toRecord();
    }

    @Override
    public void removeById(UUID id) {
        CustomerEntity toRemove = repository.findById(id).orElse(null);
        assert toRemove != null;
        repository.delete(toRemove);
    }

    static void resultShooter(List<Customer> result, List<CustomerEntity> raw) {
        raw.forEach(e -> {
            result.add(
                    new Customer(
                            e.getId(),
                            e.getName(),
                            e.getPhone(),
                            e.getIsMember()
                    )
            );
        });
    };
}
