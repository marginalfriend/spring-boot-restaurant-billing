package io.abun.wmb.TableManagement;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "m_table")
@Table(name = "m_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_seq") // Table neither secret to hide nor risk of collision, why bother using big o data-type such as UUID
    @SequenceGenerator(name = "table_seq", sequenceName = "table_seq", allocationSize = 1) // The default allocation size is 50, das crazy
    private Integer id;

    @Column(name = "table_name", nullable = false)
    @Size(min = 3, max = 3)
    private String tableName;
}
