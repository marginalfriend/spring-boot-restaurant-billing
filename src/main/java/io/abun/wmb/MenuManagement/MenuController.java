package io.abun.wmb.MenuManagement;

import io.abun.wmb.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constants.API_URL_ROOT + Constants.MENU_ENDPOINT)
public class MenuController {
    @Autowired
    MenuService service;

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
}
