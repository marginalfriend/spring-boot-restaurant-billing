package io.abun.wmb.TransactionService;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "m_trans_type")
@Table(name = "m_trans_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionTypeEntity {
    @Id
    @Size(min = 3, max = 3)
    private String id;

    @Column(nullable = false)
    private String description;
}
