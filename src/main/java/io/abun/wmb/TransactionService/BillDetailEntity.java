package io.abun.wmb.TransactionService;

import io.abun.wmb.MenuManagement.MenuEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity(name = "t_bill_detail")
@Table(name = "t_bill_detail")
@Data
public class BillDetailEntity {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "qty", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private BillEntity billID;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuEntity menuEntity;
}