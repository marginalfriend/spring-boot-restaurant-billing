package io.abun.wmb.TableManagement;

import io.abun.wmb.Constants.EntityName;
import io.abun.wmb.TransactionService.BillEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity(name = EntityName.TABLE)
@Table(name = EntityName.TABLE)
@Getter
@Setter
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

    public TableEntity(Integer id, String name, Integer capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
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
                tableRecord.name(),
                tableRecord.capacity()
        );
    }
}
