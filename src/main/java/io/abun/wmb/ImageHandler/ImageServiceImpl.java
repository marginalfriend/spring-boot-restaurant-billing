package io.abun.wmb.ImageHandler;

import io.abun.wmb.Constants.Messages;
import io.abun.wmb.ImageHandler.dto.ImageRequest;
import io.abun.wmb.ImageHandler.interfaces.ImageRepository;
import io.abun.wmb.ImageHandler.interfaces.ImageService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository   imageRepository;
    private final Path              imageDirectoryPath;

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
    public Resource getById(Integer id) {
        try {

            ImageEntity imageEntity = imageRepository.findById(id).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.NOT_FOUND + ": Image data not found")
            );
            Path imageFilePath = Paths.get(imageEntity.getPath());

            if (!Files.exists(imageFilePath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.NOT_FOUND + ": Image file not found");
            } else {
                return new UrlResource(imageFilePath.toUri());
            }

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        }
    }

    @Override
    public void deleteById(Integer id) {
        try {

            ImageEntity imageEntity = imageRepository.findById(id).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.NOT_FOUND + ": Image data not found")
            );
            Path imageFilePath = Paths.get(imageEntity.getPath());

            if (!Files.exists(imageFilePath)) {

                throw new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.NOT_FOUND + ": Image file not found");

            } else {

                Files.delete(imageFilePath);
                imageRepository.delete(imageEntity);

            }

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        }
    }

    @Override
    public ImageEntity update(ImageRequest imageToUpdate) {
        try {

            ImageEntity imageEntity = imageRepository.findById(imageToUpdate.getId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.NOT_FOUND + ": Image data not found")
            );
            Path oldImageFilePath = Paths.get(imageEntity.getPath());

            // Find the existing file
            if (!Files.exists(oldImageFilePath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.NOT_FOUND + ": Image file not found");
            } else {
                // Delete the existing file
                Files.delete(oldImageFilePath);

                // Prepare the new file for saving
                String newImageFileName = System.currentTimeMillis() + "_" + imageToUpdate.getImage().getOriginalFilename();
                Path newImageFilePath = imageDirectoryPath.resolve(newImageFileName);

                // Copy (or save, same) the new file
                Files.copy(imageToUpdate.getImage().getInputStream(), newImageFilePath);

                // Update the ImageEntity (new size, new everything)
                imageEntity.setName(newImageFileName);
                imageEntity.setSize(imageToUpdate.getImage().getSize());
                imageEntity.setContentType(imageToUpdate.getImage().getContentType());
                imageEntity.setPath(imageToUpdate.getImage().toString());

                // Save and flush them image
                return imageRepository.saveAndFlush(imageEntity);
            }

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        }
    }


}
