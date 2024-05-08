package io.abun.wmb.TransactionService.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity(name = "m_trans_type")
@Table(name = "m_trans_type")
@Data
public class TransactionTypeEntity {
    @Id
    @Size(min = 3, max = 3)
    private String id;

    @Column(nullable = false)
    private String description;
}
