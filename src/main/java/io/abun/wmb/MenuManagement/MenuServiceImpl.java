package io.abun.wmb.MenuManagement;

import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MenuServiceImpl implements MenuService{
    @Autowired
    MenuRepository repository;

    @Override
    public Menu create(Menu menu) {
        return repository.save(MenuEntity.parse(menu)).toRecord();
    }

    @Override
    public List<Menu> findAll(MenuCriteria menu) {
        if (menu == null) {
            return repository.findAll().stream().map(MenuEntity::toRecord).toList();
        }

        String  name    = menu.name();
        Integer pmin    = menu.pmin();
        Integer pmax    = menu.pmax();

        List<Menu> result = new ArrayList<>();

        Specification<MenuEntity> specifications = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + name.toLowerCase() + '%'));
            }

            if (pmin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), pmin));
            }

            if (pmax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), pmax));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        });

        resultShooter(result, repository.findAll(specifications));

        return result;
    }

    @Override
    public Menu findById(UUID id) {
        return repository.findById(id).orElseThrow().toRecord();
    }

    @Override
    public Menu update(Menu menu) {

        MenuEntity toUpdate = repository.findById(menu.id()).orElse(null);
        assert toUpdate != null;

        if (menu.name() != null) {
            toUpdate.setName(menu.name());
        }

        if (menu.price() != null) {
            toUpdate.setPrice(menu.price());
        }

        return repository.saveAndFlush(toUpdate).toRecord();
    }

    @Override
    public void removeById(UUID id) {
        MenuEntity toRemove = repository.findById(id).orElse(null);
        assert toRemove != null;
        repository.delete(toRemove);
    }

    static void resultShooter(List<Menu> result, List<MenuEntity> raw) {
        raw.forEach(e -> {
            result.add(
                    new Menu(
                            e.getId(),
                            e.getName(),
                            e.getPrice()
                    )
            );
        });
    };
}
