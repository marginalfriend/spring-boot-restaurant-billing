package io.abun.wmb.MenuManagement;

import io.abun.wmb.CustomerManagement.Customer;
import io.abun.wmb.CustomerManagement.CustomerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "m_menu")
@Table(name = "m_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_seq") // Menu has neither secret to hide nor risk of collision, why bother using big o data-type such as UUID
    @SequenceGenerator(name = "menu_seq", sequenceName = "menu_seq", allocationSize = 1) // The default allocation size is 50, das crazy
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    public Menu toRecord() {
        return new Menu(
                id,
                name,
                price
        );
    }

    public MenuEntity(UUID id, String name, Integer price) {
        this.id         = id;
        this.name       = name;
        this.price      = price;
    }

    public static MenuEntity parse(Menu menu) {
        return new MenuEntity(
                menu.id(),
                menu.name(),
                menu.price()
        );
    }
}