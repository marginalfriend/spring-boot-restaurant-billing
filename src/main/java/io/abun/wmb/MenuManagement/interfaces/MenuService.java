package io.abun.wmb.MenuManagement.interfaces;

import io.abun.wmb.MenuManagement.dto.MenuCriteria;
import io.abun.wmb.MenuManagement.dto.MenuRequest;
import io.abun.wmb.MenuManagement.dto.MenuResponse;

import java.util.List;

public interface MenuService {
    MenuResponse create(MenuRequest menuRequest);
    List<MenuResponse> findAll(MenuCriteria menu);
    MenuResponse findById(Integer id);
    MenuResponse update(MenuRequest menuRequest);
    void removeById(Integer id);
}
