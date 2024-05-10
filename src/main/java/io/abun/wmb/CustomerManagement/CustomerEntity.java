package io.abun.wmb.CustomerManagement;

import io.abun.wmb.TransactionService.BillEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity(name = "m_customer")
@Table(name = "m_customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(name = "is_member", nullable = false)
    private Boolean isMember;

    @OneToMany(mappedBy = "customer")
    private List<BillEntity> bills;

    // I bet this method will often come in handy
    public Customer toRecord() {
        return new Customer(
                id,
                name,
                phone,
                isMember
        );
    }

    public CustomerEntity(UUID id, String name, String phone, Boolean isMember) {
        this.id         = id;
        this.name       = name;
        this.phone      = phone;
        this.isMember   = isMember;
    }

    public static CustomerEntity parse(Customer customer) {
        return new CustomerEntity(
                customer.id(),
                customer.name(),
                customer.phone(),
                customer.isMember()
        );
    }
}
