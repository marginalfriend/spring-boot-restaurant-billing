package io.abun.wmb.TableManagement;

import io.abun.wmb.MenuManagement.Menu;
import io.abun.wmb.MenuManagement.MenuCriteria;

import java.util.List;
import java.util.UUID;

public interface TableService {
    TableRecord create(TableRecord table);
    List<TableRecord> findAll(TableCriteria table);
    TableRecord findById(Integer id);
    TableRecord update(TableRecord table);

    void removeById(Integer id);
}
