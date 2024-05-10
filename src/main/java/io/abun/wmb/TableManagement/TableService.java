package io.abun.wmb.TableManagement;

import java.util.List;

public interface TableService {
    TableRecord create(TableRecord table);
    List<TableRecord> findAll(TableCriteria table);
    TableRecord findById(Integer id);
    TableRecord update(TableRecord table);
    void removeById(Integer id);
}
