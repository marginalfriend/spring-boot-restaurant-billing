package io.abun.wmb.TransactionService.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity(name = "m_menu")
@Table(name = "m_menu")
@Data
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_seq") // Menu has neither secret to hide nor risk of collision, why bother using big o data-type such as UUID
    @SequenceGenerator(name = "menu_seq", sequenceName = "menu_seq", allocationSize = 1) // The default allocation size is 50, das crazy
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;
}