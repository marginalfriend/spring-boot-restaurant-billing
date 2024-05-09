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

        String name         = customer.name();
        String phone        = customer.phone();
        Boolean isMember    = customer.isMember();

        List<Customer> result = new ArrayList<>();

        Specification<CustomerEntity> specifications = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + name.toLowerCase() + '%'));
            }

            if (phone != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), '%' + phone.toLowerCase() + '%'));
            }

            if (isMember != null) {
                predicates.add(criteriaBuilder.equal(root.get("isMember"), isMember));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        });

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

        toUpdate.setName(customer.name());
        toUpdate.setPhone(customer.phone());
        toUpdate.setIsMember(customer.isMember());

        return repository.saveAndFlush(toUpdate).toRecord();
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
