package io.abun.wmb.ImageHandler.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageRequest {
    private Integer         id;
    private MultipartFile   image;
}
