package io.abun.wmb.MenuManagement;

import io.abun.wmb.Constants.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Routes.ROOT + Routes.MENUS)
public class MenuController {
    @Autowired
    MenuService service;

    @PostMapping
    public Menu create(@RequestBody Menu menu) {
        return service.create(menu);
    }

    @GetMapping
    public List<Menu> find(
            @RequestParam(name = "name", required = false) String   name,
            @RequestParam(name = "pmin", required = false) Integer  pmin,
            @RequestParam(name = "pmax", required = false) Integer  pmax
            ) {
        return service.findAll(
                new MenuCriteria(
                        name,
                        pmin,
                        pmax
                )
        );
    }

    @PutMapping
    public Menu update(@RequestBody Menu menu) {
        return service.update(menu);
    }

    @DeleteMapping
    public String delete(@RequestBody Integer id) {
        service.removeById(id);
        return "Successfully deleted a menu";
    }
}
