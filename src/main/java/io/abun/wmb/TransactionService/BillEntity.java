package io.abun.wmb.TransactionService;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.abun.wmb.Constants.EntityName;
import io.abun.wmb.CustomerManagement.CustomerEntity;
import io.abun.wmb.TableManagement.TableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity(name = EntityName.BILL)
@Table(name = EntityName.BILL)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillEntity {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "trans_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp transDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableEntity table;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<BillDetailEntity> billDetails;

    @Enumerated(EnumType.ORDINAL)
    TransactionType transactionType;
}