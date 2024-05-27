package io.abun.wmb.MenuManagement.ImageHandler;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService{
    private final ImageRepository imageRepository;
    private final Path imageDirectoryPath;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, @Value("${wmb.multipart.path_location}") String directoryPath) {
        this.imageRepository = imageRepository;
        this.imageDirectoryPath = Paths.get(directoryPath);
    }

    @PostConstruct
    public void initDirectory(){
        if(!Files.exists(imageDirectoryPath)){
            try {
                Files.createDirectory(imageDirectoryPath);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @Override
    public ImageEntity create(MultipartFile multipartFile) {
        try {
            if (!List.of("image/jpeg", "image/png", "image/jpg", "image/svg+xml").contains(multipartFile.getContentType())) {
                throw new ConstraintViolationException("invalid image format",null);
            }

            String imageFileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

            Path imageFilePath = imageDirectoryPath.resolve(imageFileName);

            Files.copy(multipartFile.getInputStream(), imageFilePath);

            ImageEntity imageEntity = ImageEntity.builder()
                    .name(imageFileName)
                    .size(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .path(imageFilePath.toString())
                    .build();

            imageRepository.saveAndFlush(imageEntity);

            return imageEntity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource getById(String id) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
