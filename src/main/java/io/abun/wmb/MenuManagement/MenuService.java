package io.abun.wmb.MenuManagement;

import java.util.List;

public interface MenuService {
    MenuResponse create(MenuRequest menuRequest);
    List<MenuResponse> findAll(MenuCriteria menu);
    MenuResponse findById(Integer id);
    MenuResponse update(MenuRequest menuRequest);

    void removeById(Integer id);
}
