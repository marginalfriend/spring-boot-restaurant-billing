package io.abun.wmb.MenuManagement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.abun.wmb.Constants.CommonResponse;
import io.abun.wmb.Constants.Messages;
import io.abun.wmb.Constants.Routes;
import io.abun.wmb.MenuManagement.dto.MenuCriteria;
import io.abun.wmb.MenuManagement.dto.MenuRequest;
import io.abun.wmb.MenuManagement.dto.MenuResponse;
import io.abun.wmb.MenuManagement.interfaces.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.ROOT + Routes.MENUS)
public class MenuController {
    MenuService service;
    ObjectMapper objectMapper;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MenuResponse>> create(
            @RequestPart(name = "menu")         String          menuJson,
            @RequestPart(name = "menuImage")    MultipartFile   image
            ) {

        CommonResponse.CommonResponseBuilder<MenuResponse> responseBuilder = CommonResponse.builder();

        try {
            MenuRequest menuRequest = objectMapper.readValue(menuJson, new TypeReference<>() {
            });

            menuRequest.setImage(image);
            MenuResponse menuResponse = service.create(menuRequest);

            responseBuilder.data(menuResponse);
            responseBuilder.message(Messages.CREATED);
            responseBuilder.statusCode(HttpStatus.CREATED.value());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBuilder.build());
        } catch (Exception e) {

            responseBuilder.message("Failed creating new product : " + e.getMessage());
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBuilder.build());
        }
    }

    @GetMapping
    public List<MenuResponse> find(
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
    public MenuResponse update(@RequestBody MenuRequest menuRequest) {
        return service.update(menuRequest);
    }

    @DeleteMapping
    public String delete(@RequestBody Integer id) {
        service.removeById(id);
        return "Successfully deleted a menu";
    }


}
