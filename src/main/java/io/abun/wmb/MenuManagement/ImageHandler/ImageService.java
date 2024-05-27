package io.abun.wmb.MenuManagement.ImageHandler;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageEntity create(MultipartFile multipartFile);

    Resource getById(String id);

    void deleteById(String id);
}
