package io.abun.wmb.CustomerManagement;

import io.abun.wmb.Auth.UserAccountEntity;
import io.abun.wmb.Auth.interfaces.UserAccountService;
import io.abun.wmb.Constants.Messages;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    CustomerRepository repository;
    UserAccountService userAccountService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer create(Customer customer) {
        return repository.save(CustomerEntity.parse(customer)).toRecord();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Customer> findAll(CustomerRequest customer) {
        String columnToSort;

        if (CustomerEntity.hasProperty(customer.sortBy())) {
            columnToSort = customer.sortBy();
        } else {
            columnToSort = "name";
        }

        Sort                            sorting         = Sort.by(Sort.Direction.fromString(customer.direction().toString()), columnToSort);
        Pageable                        pageable        = PageRequest.of((customer.page() - 1), customer.size());
        Specification<CustomerEntity>   specifications  = CustomerSpecification.getSpecification(customer);

        return repository.findAll(specifications, pageable).map(CustomerEntity::toRecord);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")
                ).toRecord();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer update(Customer customer) {
        CustomerEntity toUpdate = CustomerEntity.parse(this.findById(customer.id()));

        UserAccountEntity clientAccount = userAccountService.getByContext();

        if (!clientAccount.getId().equals(toUpdate.getUserAccount().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, Messages.AUTHENTICATION_FAILURE + ": Unauthorized");
        }

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeById(UUID id) {
        CustomerEntity toRemove = CustomerEntity.parse(this.findById(id));
        repository.delete(toRemove);
    }
}
