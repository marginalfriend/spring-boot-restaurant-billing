package io.abun.wmb.CustomerManagement;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public static Specification<CustomerEntity> getSpecification(Customer customer) {
        String name         = customer.name();
        String phone        = customer.phone();
        Boolean isMember    = customer.isMember();

        return  ((root, query, criteriaBuilder) -> {
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
    }
}
