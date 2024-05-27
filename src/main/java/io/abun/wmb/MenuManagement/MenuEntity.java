package io.abun.wmb.MenuManagement;

import io.abun.wmb.ImageHandler.ImageEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "m_menu")
@Table(name = "m_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_seq") // Menu has neither secret to hide nor risk of collision, why bother using big o data-type such as UUID
    @SequenceGenerator(name = "menu_seq", sequenceName = "menu_seq", allocationSize = 1) // The default allocation size is 50, das crazy
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @OneToOne
    @JoinColumn(name = "image_id")
    private ImageEntity image;

}