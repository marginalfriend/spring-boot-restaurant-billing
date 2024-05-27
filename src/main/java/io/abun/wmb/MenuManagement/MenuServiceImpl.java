package io.abun.wmb.MenuManagement;

import io.abun.wmb.MenuManagement.ImageHandler.MenuImageResponse;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{
    final MenuRepository repository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse create(MenuRequest menuRequest) {
        MenuEntity newMenu = MenuEntity.builder()
                .name(menuRequest.getName())
                .price(menuRequest.getPrice())

                .build();

        return repository.save(MenuEntity.parse(menuRequest)).toRecord();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MenuResponse> findAll(MenuCriteria menu) {
        if (menu == null) {
            return repository.findAll().stream().map(MenuServiceImpl::toResponse).toList();
        }

        String  name    = menu.name();
        Integer pmin    = menu.pmin();
        Integer pmax    = menu.pmax();

        List<MenuResponse> result = new ArrayList<>();

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

    @Transactional(readOnly = true)
    @Override
    public MenuResponse findById(Integer id) {
        return toResponse(repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found")
        ));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse update(MenuRequest menuRequest) {

        MenuEntity toUpdate = repository.findById(menuRequest.getId()).orElse(null);
        assert toUpdate != null;

        if (menuRequest.getName() != null) {
            toUpdate.setName(menuRequest.getName());
        }

        if (menuRequest.getPrice() != null) {
            toUpdate.setPrice(menuRequest.getPrice());
        }

        return toResponse(repository.saveAndFlush(toUpdate));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeById(Integer id) {
        MenuEntity toRemove = repository.findById(id).orElse(null);
        assert toRemove != null;

        repository.delete(toRemove);
    }

    static MenuResponse toResponse(MenuEntity entity) {

        MenuImageResponse imageResponse = new MenuImageResponse(
                entity.getImage().getFilePath(),
                entity.getImage().getName()
        );

        return new MenuResponse(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                imageResponse
        );
    }

    static void resultShooter(List<MenuResponse> result, List<MenuEntity> raw) {

        raw.forEach(e -> {
            MenuImageResponse imageResponse = new MenuImageResponse(
                    e.getImage().getFilePath(),
                    e.getImage().getName()
            );

            result.add(
                    new MenuResponse(
                            e.getId(),
                            e.getName(),
                            e.getPrice(),
                            imageResponse
                    )
            );
        });
    };
}
