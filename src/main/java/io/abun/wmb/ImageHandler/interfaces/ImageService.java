package io.abun.wmb.ImageHandler.interfaces;

import io.abun.wmb.ImageHandler.ImageEntity;
import io.abun.wmb.ImageHandler.dto.ImageRequest;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageEntity create(MultipartFile multipartFile);
    Resource getById(Integer id);
    void deleteById(Integer id);
    ImageEntity update(ImageRequest imageToUpdate);
}
