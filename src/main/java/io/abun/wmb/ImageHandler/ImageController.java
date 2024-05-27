package io.abun.wmb.ImageHandler;

import io.abun.wmb.Constants.Routes;
import io.abun.wmb.ImageHandler.interfaces.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Routes.IMAGES)
public class ImageController {
    private final ImageService imageService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> download(
            @PathVariable Integer id
    ) {
        Resource imageById = imageService.getById(id);

        String headerValue = String.format("attachment; filename=%s",imageById.getFilename());

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(headerValue)
                .body(imageById);
    }
}
