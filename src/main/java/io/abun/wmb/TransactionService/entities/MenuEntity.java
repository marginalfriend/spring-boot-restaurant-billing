package io.abun.wmb.TransactionService;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity(name = "m_menu")
@Table(name = "m_menu")
@Data
public class MenuEntity {
    private UUID id;
    private String name;
    private Integer price;
}