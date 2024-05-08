package io.abun.wmb.TransactionService.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "t_bill")
@Table(name = "t_bill")
@Data
public class BillEntity {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "trans_date")
    private Timestamp transDate;

    @ManyToMany
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableEntity table;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TransactionTypeEntity transactionType;
}