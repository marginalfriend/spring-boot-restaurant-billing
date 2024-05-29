package io.abun.wmb.TableManagement;

import io.abun.wmb.Constants.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Routes.TABLES)
public class TableController {
    @Autowired
    TableService service;

    @GetMapping
    public List<TableRecord> findAll(
            @RequestParam(name = "name",    required = false)   String  name,
            @RequestParam(name = "minCap",  required = false)   Integer minCap,
            @RequestParam(name = "maxCap",  required = false)   Integer maxCap
    ) {
        return service.findAll(
                new TableCriteria(
                        name,
                        minCap,
                        maxCap
                )
        );
    }

    @PostMapping
    public TableRecord create(@RequestBody TableRecord table) {
        return service.create(table);
    }

    @PutMapping
    public TableRecord update(@RequestBody TableRecord table) {
        return service.update(table);
    }

    @DeleteMapping
    public String delete(@RequestBody Integer id) {
        service.removeById(id);

        return "Removed a table";
    }
}
