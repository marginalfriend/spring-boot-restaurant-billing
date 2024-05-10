package io.abun.wmb.MenuManagement;

import io.abun.wmb.CustomerManagement.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface MenuService {
    Menu create(Menu menu);
    List<Menu> findAll(MenuCriteria menu);
    Menu findById(UUID id);
    Menu update(Menu menu);

    void removeById(UUID id);
}
