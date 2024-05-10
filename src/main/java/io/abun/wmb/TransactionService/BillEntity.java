package io.abun.wmb.TransactionService;

import io.abun.wmb.CustomerManagement.CustomerEntity;
import io.abun.wmb.TableManagement.TableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "t_bill")
@Table(name = "t_bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillEntity {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "trans_date")
    private Timestamp transDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableEntity table;

    @Enumerated(EnumType.ORDINAL)
    TransactionType transactionType;
}