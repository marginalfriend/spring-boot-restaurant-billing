package io.abun.wmb.TableManagement;

import io.abun.wmb.TransactionService.BillEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Column(name = "name", nullable = false)
    @Size(min = 3, max = 3)
    private String name;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "table")
    private List<BillEntity> bills;

    public TableEntity(Integer id, String name) {
        this.id         = id;
        this.name = name;
    }

    public TableRecord toRecord() {
        return new TableRecord(
                this.id,
                this.name,
                this.capacity
        );
    }

    public static TableEntity parse(TableRecord tableRecord) {
        return new TableEntity(
                tableRecord.id(),
                tableRecord.name()
        );
    }
}
