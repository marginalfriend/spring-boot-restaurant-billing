package io.abun.wmb.TransactionService;

import io.abun.wmb.Constants.EntityName;
import io.abun.wmb.MenuManagement.MenuEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity(name = EntityName.BILL_DETAILS)
@Table(name = EntityName.BILL_DETAILS)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_detail_seq") // BillDetail has neither secret to hide nor risk of collision, why bother using big o data-type such as UUID
    @SequenceGenerator(name = "bill_detail_seq", sequenceName = "bill_detail_seq", allocationSize = 1) // The default allocation size is 50, das crazy
    private Integer id;

    @Column(name = "qty", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private BillEntity bill;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuEntity menu;
}