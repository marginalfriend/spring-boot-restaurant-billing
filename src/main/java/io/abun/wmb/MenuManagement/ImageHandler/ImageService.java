package io.abun.wmb.MenuManagement.ImageHandler;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageEntity create(MultipartFile multipartFile);

    Resource getById(Integer id);

    void deleteById(Integer id);
}
