package io.abun.wmb.TableManagement;

import io.abun.wmb.MenuManagement.MenuEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TableServiceImpl implements TableService{
    @Autowired
    TableRepository repository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TableRecord create(TableRecord table) {
        return repository.saveAndFlush(TableEntity.parse(table)).toRecord();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TableRecord> findAll(TableCriteria table) {
        if (table == null) {
            return repository.findAll().stream().map(TableEntity::toRecord).toList();
        }

        String  name    = table.name();
        Integer minCap  = table.minCap();
        Integer maxCap  = table.maxCap();

        List<TableRecord> result = new ArrayList<>();

        Specification<TableEntity> specifications = (((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + name.toLowerCase() + '%'));
            }

            if (minCap != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("capacity"), minCap));
            }

            if (maxCap != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("capacity"), minCap));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        }));

        resultShooter(result, repository.findAll(specifications));

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public TableRecord findById(Integer id) {
        return repository.findById(id).orElse(null).toRecord();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TableRecord update(TableRecord table) {

        TableEntity toUpdate = repository.findById(table.id()).orElse(null);
        assert toUpdate != null;

        if (table.name() != null) {
            toUpdate.setName(table.name());
        }

        if (table.capacity() != null) {
            toUpdate.setCapacity(table.capacity());
        }

        return repository.saveAndFlush(toUpdate).toRecord();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeById(Integer id) {
        TableEntity toRemove = repository.findById(id).orElse(null);
        assert toRemove != null;

        repository.delete(toRemove);
    }

    static void resultShooter(List<TableRecord> result, List<TableEntity> raw) {
        raw.forEach(e -> {
            result.add(
                    new TableRecord(
                            e.getId(),
                            e.getName(),
                            e.getCapacity()
                    )
            );
        });
    }
}
