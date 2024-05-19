package io.abun.wmb.CustomerManagement;

import io.abun.wmb.Auth.UserAccount;
import io.abun.wmb.TransactionService.BillEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class CustomerEntity {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(unique = true)
    private String phone;

    @Column(name = "is_member", nullable = false)
    private Boolean isMember;

    @OneToMany(mappedBy = "customer")
    private List<BillEntity> bills;

    @OneToOne
    @JoinColumn(name = "user_account_id", unique = true)
    private UserAccount userAccount;

    public static boolean hasProperty(String property) {
        return switch (property) {
            case "isMember", "phone", "name", "id" -> true;
            default -> false;
        };
    }

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
